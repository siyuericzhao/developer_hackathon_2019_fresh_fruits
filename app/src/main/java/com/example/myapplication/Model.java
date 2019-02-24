package com.example.myapplication;


import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.ModelVersion;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Prediction;
import com.google.gson.*;


import java.io.File;
import java.net.HttpURLConnection;
import java.util.List;

import static com.example.myapplication.CanonAPI.url;

public class Model {
    private ClarifaiClient client;
    private String modelId;
    private String versionId;

    /**
     * Constructor
     *
     * @param apiKey    Api Key from Clarifai
     * @param modelId   Model Id
     * @param versionId Version Id
     */
    public Model(String apiKey, String modelId, String versionId) {
        client = new ClarifaiBuilder(apiKey).buildSync();

        this.modelId = modelId;
        this.versionId = versionId;

        ModelVersion modelVersion = client.getModelVersionByID(modelId, versionId)
                .executeSync()
                .get();
    }

    /**
     * Predicts by a given image URL
     *
     * @param url Url to image
     */
    public boolean predictImageByURL(String url) {
        System.out.printf("Making prediction for image url: %s\n", url);
        ClarifaiResponse<List<ClarifaiOutput<Prediction>>> response = client.predict(this.modelId)
                .withVersion(this.versionId)
                .withInputs(ClarifaiInput.forImage(url))
                .executeSync();
        String jsonResponse = response.rawBody();
        Double confidence = getPrediction(jsonResponse);
        return isFresh(0.5, confidence);
    }

    /**
     * Makes a prediction of a given image file
     *
     * @param absPath Absolute path to image
     * @return Boolean of freshness of an orange
     */
    public boolean predictImageByFilePath(String absPath) {
        System.out.printf("Making prediction for image: %s\n", absPath);
        ClarifaiResponse<List<ClarifaiOutput<Prediction>>> response = client.predict(this.modelId)
                .withVersion(this.versionId)
                .withInputs(ClarifaiInput.forImage(new File(absPath)))
                .executeSync();
        String jsonResponse = response.rawBody();
        Double confidence = getPrediction(jsonResponse);
        System.out.println("Confidence of freshness: " + confidence);
        return isFresh(0.5, confidence);
    }

    /**
     * Fresh if above threshold value
     *
     * @param threshold  Threshold to be higher than
     * @param confidence Confidence of freshness
     * @return Boolean whether it is fresh
     */
    private boolean isFresh(double threshold, double confidence) {
        if (confidence - threshold > 0.0001) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Parses the returned json object
     *
     * @param jsonResponse Raw json string from of the prediction
     * @return Return the confidence value of freshness
     */
    private double getPrediction(String jsonResponse) {
        JsonElement jElement = new JsonParser().parse(jsonResponse);
        JsonObject jObject = jElement.getAsJsonObject();
        JsonArray jsonArray = jObject.getAsJsonArray("outputs");
        jObject = jsonArray.get(0).getAsJsonObject();

        jObject = jObject.getAsJsonObject("data");
        jsonArray = jObject.getAsJsonArray("concepts");

        jObject = jsonArray.get(0).getAsJsonObject();
        return Double.valueOf(jObject.get("value").getAsString());
    }
}