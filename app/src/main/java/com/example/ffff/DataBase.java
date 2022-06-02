package com.example.ffff;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class DataBase {
    private static final String DATABASE_NAME = "simple.db";
    private static final int DATABASE_VERSION = 1;
    private static final String id_column = "id";
    private static final String name_column = "name";
    private SQLiteDatabase db;
    //Days table
    private static final String days_table_name = "Days";
    private static final String days_table_day = "day";

    //product table
    private static final String product_table_name = "Products";
    private static final String product_weight_column = "weight";
    private static final String product_vrgCalories_column = "vrg_calories";

    //Ingredients table
    private static final String ingredients_table_name = "Ingredients";
    private static final String ingredients_type_column = "type_of_ingredient";
    private static final String ingredients_caloriesPerGram_column = "calories_per_gramm";

    //Products_in_day table
    private static final String productsInDay_table_name = "Products_in_day";
    private static final String productsInDay_idDay_column = "id_day";
    private static final String productsInDay_idProducts_column = "id_products";

    //Ingredients_in_product table
    private static final String ingredientsInProduct_table_name = "Ingredients_in_product";
    private static final String ingredientsInProduct_idProduct_column = "id_product";
    private static final String ingredientsInProduct_idIngredient_column = "id_ingredient";
    private static final String ingredientsInProduct_methodOfCookId_column = "method_of_cook_id";
    private static final String ingredientsInProduct_relativeWeight_column = "relative_weight";

    //Type_of_ingredient table
    private static final String typeOfIngredient_table_name = "Type_of_ingredient";
    private static final String typeOfIngredient_ingredientType_column = "ingredient_type";

    //Method_of_cook table
    private static final String methodOfCook_table_name = "Method_of_cook";
    private static final String methodOfCook_methodOfCook_column = "method_of_cook";

    public DataBase(Context context) {
        OpenHelper openHelper = new OpenHelper(context);
        db = openHelper.getWritableDatabase();
    }
    public void remove_ingredient(Ingredient in) {
        db.delete(ingredients_table_name,name_column+"=?",new String[]{in.name});
    }
    public void get_ingredients(ArrayList<Ingredient> arr) {
        Cursor getIngredientsTable = db.query(ingredients_table_name,
                new String[]{id_column, name_column, ingredients_type_column, ingredients_caloriesPerGram_column}, null, null, null, null, null);
        Cursor typesOfIngredients;
        getIngredientsTable.moveToFirst();
        int i = 0;
        if (!getIngredientsTable.isAfterLast()) {
            do {
                arr.add(i,new Ingredient());
                arr.get(i).name = getIngredientsTable.getString(1);
                typesOfIngredients = db.query(typeOfIngredient_table_name,
                        new String[]{typeOfIngredient_ingredientType_column},
                        id_column + " = "+getIngredientsTable.getString(2),
                        null,null, null, null);
                typesOfIngredients.moveToFirst();
                if (!typesOfIngredients.isAfterLast()) {
                    arr.get(i).type_of_ingredient = typesOfIngredients.getString(0);
                }
                arr.get(i).calories_per_gram = getIngredientsTable.getFloat(3);
                i++;
            } while (getIngredientsTable.moveToNext());
            typesOfIngredients.close();
        }
        getIngredientsTable.close();
    }
    public ArrayList<Product>get_products(ArrayList<Product>arr){
        Cursor products_table = db.query(product_table_name,
                null,null,null,null,null,null);
        products_table.moveToFirst();
        if(!products_table.isAfterLast()){
            int i = 1;
            do{
                arr.get(i).name = products_table.getString(1);
                arr.get(i).weight = products_table.getInt(2);
                arr.get(i).calories_per_gram = products_table.getFloat(3);
                i++;
            }while(products_table.moveToNext());
            products_table.close();
        }
        return arr;
    }
    public void add_ingredient(Ingredient ingredient){
        ContentValues cv = new ContentValues();
        cv.put(name_column,ingredient.name);
        cv.put(ingredients_caloriesPerGram_column,ingredient.calories_per_gram);
        cv.put(ingredients_type_column, ingredient.type_of_ingredient_id);
        db.insert(ingredients_table_name,null,cv);
    }
    public Product add_product(String name, ArrayList<Ingredient>composition){
        ContentValues cv = new ContentValues();
        long sum_weight = 0;
        for (int i = 0; i< composition.size();i++){
            sum_weight += composition.get(i).relWeight;
        }
        for (int i = 0; i< composition.size();i++){

        }
    }
    class OpenHelper extends SQLiteOpenHelper{
        private Context ctx;
        public OpenHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            ctx = context;
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String  query = "CREATE TABLE " + typeOfIngredient_table_name + " (" +
                    id_column + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    typeOfIngredient_ingredientType_column+ " TEXT UNIQUE NOT NULL "+
                    //"FOREIGN KEY("+id_column+") REFERENCES "+ name_column+"("+ingredients_type_column+")"+
                    ");";
            db.execSQL(query);
            query = "CREATE TABLE " + methodOfCook_table_name + " (" +
                    id_column + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    methodOfCook_methodOfCook_column + " TEXT " +");";
            db.execSQL(query);
            query ="CREATE TABLE " + ingredientsInProduct_table_name + " (" +
                    id_column + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ingredientsInProduct_idProduct_column + " INTEGER NOT NULL, " +
                    ingredientsInProduct_idIngredient_column + " INTEGER NOT NULL, " +
                    ingredientsInProduct_methodOfCookId_column + " INTEGER NOT NULL, " +
                    ingredientsInProduct_relativeWeight_column + " FLOAT NOT NULL," +
                    "FOREIGN KEY("+ingredientsInProduct_methodOfCookId_column+") REFERENCES "+ methodOfCook_table_name+"("+id_column+"));";
            db.execSQL(query);
            query = "CREATE TABLE "+ productsInDay_table_name+" (" +
                    id_column + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    productsInDay_idDay_column + " INTEGER NOT NULL, " +
                    productsInDay_idProducts_column + " INTEGER NOT NULL, "+

                    "FOREIGN KEY("+productsInDay_idProducts_column+") REFERENCES "+ product_table_name+"("+id_column+"));";
            db.execSQL(query);query = "CREATE TABLE " + ingredients_table_name + " (" +
                    id_column + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    name_column +" TEXT NOT NULL UNIQUE, " +
                    ingredients_type_column + " INTEGER NOT NULL," +
                    ingredients_caloriesPerGram_column + " REAL, "+
                    "FOREIGN KEY("+id_column+") REFERENCES "+ ingredientsInProduct_table_name+"("+ingredientsInProduct_idIngredient_column+"), "+
                    "FOREIGN KEY("+ingredients_type_column+") REFERENCES "+ typeOfIngredient_table_name+"("+id_column+")"+
                    ");";
            db.execSQL(query);
            query = "CREATE TABLE " +product_table_name+"(" +
                    id_column +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    name_column + " TEXT NOT NULL UNIQUE, "+
                    //product_weight_column + " INTEGER, "+
                    product_vrgCalories_column + " REAL, "+
                    "FOREIGN KEY("+id_column+") REFERENCES "+ingredientsInProduct_table_name +"("+ingredientsInProduct_idProduct_column+")"+
                    ");";
            db.execSQL(query);
            query = "CREATE TABLE "+ days_table_name+"("+
                    id_column + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    days_table_day + " TEXT NOT NULL UNIQUE, "+
                    "FOREIGN KEY("+id_column+") REFERENCES "+ productsInDay_table_name+"("+productsInDay_idProducts_column+"));";
            db.execSQL(query);
            String [] ingredients_name = ctx.getResources().getStringArray(R.array.ingredient_types);
            ContentValues cv;
            for(int i = 0;i<ingredients_name.length;i++) {
                cv = new ContentValues();
                cv.put(typeOfIngredient_ingredientType_column, ingredients_name[i]);
                db.insert(typeOfIngredient_table_name, null, cv);
            }
            String[]methods_of_cook = ctx.getResources().getStringArray(R.array.methods_of_cooking);
            for (int i = 0;i<methods_of_cook.length-1;i++){
                cv= new ContentValues();
                cv.put(methodOfCook_methodOfCook_column,methods_of_cook[i]);
                db.insert(methodOfCook_table_name, null, cv);
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS " + productsInDay_table_name);
            db.execSQL("DROP TABLE IF EXISTS " + ingredientsInProduct_table_name );
            db.execSQL("DROP TABLE IF EXISTS " + typeOfIngredient_table_name);
            db.execSQL("DROP TABLE IF EXISTS " + methodOfCook_table_name);
            db.execSQL("DROP TABLE IF EXISTS " + ingredients_table_name);
            db.execSQL("DROP TABLE IF EXISTS " + product_table_name);
            db.execSQL("DROP TABLE IF EXISTS " + days_table_name);
            onCreate(db);
        }
    }
}
