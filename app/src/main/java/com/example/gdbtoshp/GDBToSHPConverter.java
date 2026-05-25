package com.example.gdbtoshp;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GDBToSHPConverter {

    public interface ConversionListener {
        void onProgress(int current, int total);
        void onComplete(String outputPath);
        void onError(String error);
    }

    private ConversionListener listener;

    public void setListener(ConversionListener listener) {
        this.listener = listener;
    }

    public void convert(File gdbFile, File outputDir) {
        new Thread(() -> {
            try {
                Map<String, Object> params = new HashMap<>();
                params.put("url", gdbFile.toURI().toURL());
                
                DataStore dataStore = DataStoreFinder.getDataStore(params);
                if (dataStore == null) {
                    notifyError("无法打开GDB文件");
                    return;
                }

                String[] typeNames = dataStore.getTypeNames();
                if (typeNames.length == 0) {
                    notifyError("GDB文件中没有找到图层");
                    dataStore.dispose();
                    return;
                }

                for (int i = 0; i < typeNames.length; i++) {
                    String typeName = typeNames[i];
                    FeatureSource<SimpleFeatureType, SimpleFeature> source = 
                        dataStore.getFeatureSource(typeName);
                    
                    CoordinateReferenceSystem crs = source.getSchema().getCoordinateReferenceSystem();
                    
                    FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures();
                    
                    File outputFile = new File(outputDir, typeName + ".shp");
                    
                    Map<String, Serializable> shpParams = new HashMap<>();
                    shpParams.put("url", outputFile.toURI().toURL());
                    shpParams.put("create spatial index", Boolean.TRUE);
                    
                    ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
                    ShapefileDataStore shpDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(shpParams);
                    
                    shpDataStore.createSchema(source.getSchema());
                    
                    if (crs != null) {
                        shpDataStore.forceSchemaCRS(crs);
                    }
                    
                    String shpTypeName = shpDataStore.getTypeNames()[0];
                    FeatureStore<SimpleFeatureType, SimpleFeature> featureStore = 
                        (FeatureStore<SimpleFeatureType, SimpleFeature>) shpDataStore.getFeatureSource(shpTypeName);
                    
                    featureStore.setTransaction(Transaction.AUTO_COMMIT);
                    featureStore.addFeatures(collection);
                    
                    shpDataStore.dispose();
                    
                    if (listener != null) {
                        listener.onProgress(i + 1, typeNames.length);
                    }
                }

                dataStore.dispose();
                
                if (listener != null) {
                    listener.onComplete(outputDir.getAbsolutePath());
                }

            } catch (Exception e) {
                notifyError("转换错误: " + e.getMessage());
            }
        }).start();
    }

    private void notifyError(String error) {
        if (listener != null) {
            listener.onError(error);
        }
    }
}
