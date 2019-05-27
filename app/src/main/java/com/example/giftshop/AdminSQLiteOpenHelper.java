package com.example.giftshop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "giftshop.db";

    // User table name
    private static final String TABLE_USER = "user";

    // Product table name
    private static final String TABLE_PRODUCT = "product";

    // Cart table name
    private static final String TABLE_CART = "cart";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_TYPE = "user_type";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    // Product Table Columns names
    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_PRODUCT_NAME = "product_name";
    private static final String COLUMN_PRODUCT_PRICE = "product_price";
    private static final String COLUMN_PRODUCT_DESCRIPTION = "product_description";
    private static final String COLUMN_PRODUCT_CATEGORY = "product_category";

    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_TYPE + " INTEGER,"
            + COLUMN_USER_PASSWORD + " TEXT" + ")";

    private String CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_PRODUCT + "("
            + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_PRODUCT_NAME + " TEXT,"
            + COLUMN_PRODUCT_PRICE + " INTEGER,"
            + COLUMN_PRODUCT_DESCRIPTION + " TEXT,"
            + COLUMN_PRODUCT_CATEGORY + " TEXT" + ")";

    private String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
            + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_ID + " INTEGER,"
            + COLUMN_PRODUCT_NAME + " TEXT,"
            + COLUMN_PRODUCT_PRICE + " INTEGER,"
            + COLUMN_PRODUCT_DESCRIPTION + " TEXT,"
            + COLUMN_PRODUCT_CATEGORY + " TEXT" + ")";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private String DROP_PRODUCT_TABLE = "DROP TABLE IF EXISTS " + TABLE_PRODUCT;
    private String DROP_CART_TABLE = "DROP TABLE IF EXISTS " + TABLE_CART;


    /**
     * Constructor
     *
     * @param context
     */
    public AdminSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL("INSERT INTO user('user_name', 'user_type', 'user_password') values('admin', '1', '123')");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_PRODUCT_TABLE);
        db.execSQL(DROP_CART_TABLE);
        // Create tables again
        onCreate(db);
    }

    /**
     * This method is to create user record
     *
     * @param cart
     */
    public void addCart(Product cart, String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, id);
        values.put(COLUMN_PRODUCT_NAME, cart.getName());
        values.put(COLUMN_PRODUCT_PRICE, cart.getPrice());
        values.put(COLUMN_PRODUCT_DESCRIPTION, cart.getDescription());
        values.put(COLUMN_PRODUCT_CATEGORY, cart.getCategory());
        // Inserting Row
        db.insert(TABLE_CART, null, values);
        db.close();
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_TYPE, user.getType());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getName());
        values.put(COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(COLUMN_PRODUCT_DESCRIPTION, product.getDescription());
        values.put(COLUMN_PRODUCT_CATEGORY, product.getCategory());
        // Inserting Row
        db.insert(TABLE_PRODUCT, null, values);
        db.close();
    }

    public List<Product> getUserCart(String idSearch) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_PRODUCT_ID,
                COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_PRICE,
                COLUMN_PRODUCT_DESCRIPTION,
                COLUMN_PRODUCT_CATEGORY
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_ID + " = ?";

        // selection argument
        String[] selectionArgs = {idSearch};

        List<Product> productList = new ArrayList<>();

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_name = name;
         **/
        Cursor cursor = db.query(TABLE_CART, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_ID)));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME));
                int price = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_PRICE)));
                String description = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_DESCRIPTION));
                String category = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_CATEGORY));

                Product product = new Product(id, name, price, description, category);

                // Adding user record to list
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return productList;
    }


    public void deleteCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete product record by id
        db.delete(TABLE_CART, null, null);
        db.close();
    }

    /**
     * This method is to fetch all product and return the list of user records
     *
     * @return list
     */
    public List<Product> getAllProduct() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_PRODUCT_ID,
                COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_PRICE,
                COLUMN_PRODUCT_DESCRIPTION,
                COLUMN_PRODUCT_CATEGORY
        };
        // sorting orders
        String sortOrder =
                COLUMN_PRODUCT_NAME + " ASC";
        List<Product> productList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_PRODUCT, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_ID)));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME));
                int price = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_PRICE)));
                String description = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_DESCRIPTION));
                String category = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_CATEGORY));

                Product product = new Product(id, name, price, description, category);

                // Adding user record to list
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return productList;
    }

    /**
     * This method is to delete product record
     *
     * @param id
     */
    public void deleteProduct(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete product record by id
        db.delete(TABLE_PRODUCT, COLUMN_PRODUCT_ID + " = ?",
                new String[]{id});
        db.close();
    }

    public void updateProduct(String id, String name, String price, String description, String category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, name);
        values.put(COLUMN_PRODUCT_PRICE, price);
        values.put(COLUMN_PRODUCT_DESCRIPTION, description);
        values.put(COLUMN_PRODUCT_CATEGORY, category);

        // updating row
        db.update(TABLE_PRODUCT, values, COLUMN_PRODUCT_ID + " = ?",
                new String[]{id});
        db.close();
    }

    /**
     * This method to check user exist or not
     *
     * @param name
     * @return true/false
     */
    public boolean checkUser(String name) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?";

        // selection argument
        String[] selectionArgs = {name};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_name = name;
         **/
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param name
     * @param password
     * @return true/false
     */
    public boolean checkUser(String name, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {name, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_name = name AND user_password = password;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public int getUserId(String name, String password) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };

        int type = 0;
        int id = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {name, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_name = name AND user_password = password;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user id
        return id;
    }

    public int getUserType(String name, String password) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_TYPE
        };

        int type = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {name, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_name = name AND user_password = password;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                type = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_TYPE)));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return type;
    }

}
