package com.example.mithilesh.healthportal.Helpers;

import android.location.Location;
import android.util.Log;

import com.example.mithilesh.healthportal.Model.Place;
import com.example.mithilesh.healthportal.Model.PlaceDetail;
import com.example.mithilesh.healthportal.Model.PlaceLocation;
import com.example.mithilesh.healthportal.Model.PlacePhoto;
import com.example.mithilesh.healthportal.Model.PlaceReviews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public  class DataFetcher {
    private static final String TAG = "DataFetcher";

    //TODO set your google api key in place of Constatnts,API_kEY
    private static final String API_KEY = Constants.API_KEY;

    private static final String NEARBY_SEARCH_BASE_URL =
            "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";

    private static final String DETAIL_SEARCH_BASE_URL =
            "https://maps.googleapis.com/maps/api/place/details/json?placeid=";

    private Location location;
    private String radius;
    private String type ;
    private String keyWord ;


    public DataFetcher(Location location,String type, int radius, String keyWord) {
        this.location = location;
        if (radius == -1) radius = 100000;
        this.radius = String.valueOf(radius);
        this.type = type;
        this.keyWord = keyWord;
    }

    public DataFetcher() {
    }

    public String getDetailSearchEndPoint(String placeId){

        return DETAIL_SEARCH_BASE_URL +
                placeId +
                "&key=" + API_KEY;
    }

    public String getNearbySearchEndPoint(Location location,String radius,String type,String keyWord){

         return NEARBY_SEARCH_BASE_URL +
                String.valueOf(location.getLatitude()) +
                "," +
                String.valueOf(location.getLongitude()) +
                "&radius=" + String.valueOf(radius) +
                "&type=" + type +
                "&keyword=" + keyWord +
                "&key=" + API_KEY;

    }

    private byte[] getUrlBytes(String urlSpecs) throws IOException {

        Log.i(TAG, "getUrlBytes: ");
        URL url = new URL(urlSpecs);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {

            InputStream inputStream = connection.getInputStream();

            Log.i(TAG, "getUrlBytes: response code "+connection.getResponseCode());
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException("Cannot connect to api");
            }

            byte[] buffer = new byte[1024];

            int byteResult = 0;

            Log.i(TAG, "getUrlBytes: writing to outputstream");
            while ((byteResult = inputStream.read(buffer))>0){
                outputStream.write(buffer,0,byteResult);
            }

            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            connection.disconnect();
        }
        return outputStream.toByteArray();
    }

    protected String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    protected boolean containsParam(JSONObject object, String param){
        boolean result = object.has(param);
        Log.d(TAG, "containsParam: "+param);
        return result;
    }

    public  List<Place> getNearbyPlace(){
        Log.d(TAG, "getNearbyPlace: starting");
        try{

            String endPoint = getNearbySearchEndPoint(
                    location,
                    radius,
                    type,
                    keyWord
            );

            Log.d(TAG, "getNearbyPlace: endpoint: "+endPoint);

            String json = getUrlString(endPoint);


            JSONObject body = new JSONObject(json);

            Log.d(TAG, "getNearbyPlace: json body: "+body);

            return parsePlace(body);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected List<Place> parsePlace(JSONObject body) {
        Log.d(TAG, "parsePlace: starting");
        List<Place> places = new ArrayList<>();
        try{
            if (body.has("results")){

                JSONArray results = body.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    Place currentPlace = new Place();
                    JSONObject placeObject = results.getJSONObject(i);

                    //geometry
                    if (containsParam(placeObject,"geometry")){
                        //get geometry object
                        JSONObject geometryObject = placeObject.getJSONObject("geometry");

                        //extract location object from geometry
                        if (containsParam(geometryObject,"location")){
                            JSONObject locationObject = geometryObject.getJSONObject("location");
                            Double lat = locationObject.getDouble("lat");
                            Double lng = locationObject.getDouble("lng");
                            currentPlace.setLocation(lat,lng);
                        }

                    }

                    //icon url
                    if (containsParam(placeObject,"icon")) {
                        currentPlace.setIconUrl(placeObject.getString("icon"));
                    }

                    //id
                    if (containsParam(placeObject,"id")) {
                        currentPlace.setId(placeObject.getString("id"));
                    }

                    //name
                    if (containsParam(placeObject,"name")) {
                        currentPlace.setName(placeObject.getString("name"));
                    }

                    //opening hours
                    if (containsParam(placeObject,"opening_hours")) {
                        currentPlace.setOpenNow(placeObject.getJSONObject("opening_hours")
                                .getBoolean("open_now"));
                    }

                    //place id
                    if (containsParam(placeObject,"place_id")) {
                        currentPlace.setPlaceId(placeObject.getString("place_id"));
                    }

                    //rating
                    if (containsParam(placeObject,"rating")) {
                        currentPlace.setRating(placeObject.getDouble("rating"));
                    }

                    //user total rating
                    if (containsParam(placeObject,"user_ratings_total")) {
                        currentPlace.setUserRatingsTotal(placeObject.getInt("user_ratings_total"));
                    }

                    //vicinity
                    if (containsParam(placeObject,"vicinity")) {
                        currentPlace.setVicinity(placeObject.getString("vicinity"));
                    }

                    Log.d(TAG, "parsePlace: adding place object: "+currentPlace);
                    places.add(currentPlace);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "parsePlace: returning place list: "+places);
        return places;
    }

    public PlaceDetail getPlaceDetail(String place_id) {
        Log.d(TAG, "getPlaceDetail: starting");
        try{
            String endPoint = getDetailSearchEndPoint(place_id);
            Log.d(TAG, "getPlaceDetail: endpoint: "+endPoint);
            String jsonBody = getUrlString(endPoint);
            Log.d(TAG, "getPlaceDetail: jsonBody: "+jsonBody);
            JSONObject body = new JSONObject(jsonBody);
            return parsePlaceDetail(body);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private PlaceDetail parsePlaceDetail(JSONObject body) {
        Log.d(TAG, "parsePlaceDetail: starting");
        try{

            if (body.has("result")){
                JSONObject resultBody = body.getJSONObject("result");
                Log.d(TAG, "parsePlaceDetail: contains result "+resultBody);
                PlaceDetail currentPlaceDetail = new PlaceDetail();

                //formatted address
                if (containsParam(resultBody,"formatted_address")){
                    currentPlaceDetail.setFormattedAddress(resultBody.getString("formatted_address"));

                }

                //formatted phone number
                if (containsParam(resultBody,"formatted_phone_number")) {
                    currentPlaceDetail.setFormattedPhoneNumber(resultBody.getString("formatted_phone_number"));
                }

                //id
                if (containsParam(resultBody,"id")) {
                    currentPlaceDetail.setId(resultBody.getString("id"));
                }

                //international phone number
                if (containsParam(resultBody,"international_phone_number")) {
                    currentPlaceDetail.setInternationalPhoneNumber(resultBody.getString("international_phone_number"));
                }

                //name
                if (containsParam(resultBody,"name")) {
                    currentPlaceDetail.setName(resultBody.getString("name"));
                }

                //place id
                if (containsParam(resultBody,"place_id")) {
                    currentPlaceDetail.setPlaceId(resultBody.getString("place_id"));
                }

                //rating
                if (containsParam(resultBody,"rating")) {
                    currentPlaceDetail.setRating(resultBody.getInt("rating"));
                }

                //url
                if (containsParam(resultBody,"url")) {
                    currentPlaceDetail.setUrl(resultBody.getString("url"));
                }

                //user rating total
                if (containsParam(resultBody,"user_ratings_total")) {
                    currentPlaceDetail.setUserRatingsTotal(resultBody.getInt("user_ratings_total"));
                }

                //vicinity
                if (containsParam(resultBody,"vicinity")) {
                    currentPlaceDetail.setVicinity(resultBody.getString("vicinity"));
                }

                //website
                if (containsParam(resultBody,"website")) {
                    currentPlaceDetail.setWebsite(resultBody.getString("website"));
                }

                //opening hours
                if (containsParam(resultBody,"opening_hours")){
                    //get parent object
                    JSONObject openingHoursObject = resultBody.getJSONObject("opening_hours");

                    //get weekdays text
                    JSONArray weekDaysText = openingHoursObject.getJSONArray("weekday_text");
                    List<String> weekDaysTextList = new ArrayList<>();

                    //extracting string from weekdays text
                    for (int i = 0; i <weekDaysText.length() ; i++) {
                        weekDaysTextList.add(String.valueOf(weekDaysText.get(i)));
                    }

                    currentPlaceDetail.setOpenWeekdayText(weekDaysTextList);

                }

                //photos
                if (containsParam(resultBody,"photos")){


                    //get photos array
                    JSONArray photosArray = resultBody.getJSONArray("photos");

                    //initialize photos array
//                       PlacePhoto[] placePhotos = new PlacePhoto[photosArray.length()];
                    List<PlacePhoto> placePhotos = new ArrayList<>();

                    for (int i = 0; i < photosArray.length(); i++) {
                        PlacePhoto currentPhoto = new PlacePhoto();
                        //get json photo object
                        JSONObject currentJsonPhoto = (JSONObject) photosArray.get(i);

                        //height
                        if (containsParam(currentJsonPhoto,"height"))
                            currentPhoto.setHeight(currentJsonPhoto.getInt("height"));

                        //width
                        if (containsParam(currentJsonPhoto,"width"))
                            currentPhoto.setWidth(currentJsonPhoto.getInt("width"));

                        //photo reference
                        if (containsParam(currentJsonPhoto,"photo_reference"))
                            currentPhoto.setPhotoRefrence(currentJsonPhoto.getString("photo_reference"));

                        //add this current photo to array
                        placePhotos.add(currentPhoto);
                    }


                    // set this array to current place detail object

                    Log.d(TAG, "parsePlaceDetail: Setting photos "+photosArray);
                    currentPlaceDetail.setPhotos(placePhotos);

                }


                //reviews
                if (containsParam(resultBody,"reviews")){

                    //Reviews Array
                    JSONArray placeReviewsArray = resultBody.getJSONArray("reviews");
                    List<PlaceReviews> placeReviews = new ArrayList<>();

                    for (int i = 0; i < placeReviewsArray.length(); i++) {

                        PlaceReviews currentReviewObject = new PlaceReviews();
                        JSONObject currentReviewJson = (JSONObject) placeReviewsArray.get(i);
                        //author name
                        if (containsParam(currentReviewJson,"author_name"))
                            currentReviewObject.setAuthorName(currentReviewJson.getString("author_name"));

                        //author text
                        if (containsParam(currentReviewJson,"text"))
                            currentReviewObject.setText(currentReviewJson.getString("text"));

                        //rating
                        if (containsParam(currentReviewJson,"rating"))
                            currentReviewObject.setRating(currentReviewJson.getInt("rating"));

                        placeReviews.add(currentReviewObject);

                    }

                    Log.d(TAG, "parsePlaceDetail: Reviews Size: "+placeReviews.size());
                    Log.d(TAG, "parsePlaceDetail: Reviews: "+placeReviews);

                    currentPlaceDetail.setPlaceReviews(placeReviews);


                }


                Log.d(TAG, "parsePlaceDetail: returning parsed data");
                // return curent place details object
                return currentPlaceDetail;

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //if there is no result return null
        return null;
    }


}
