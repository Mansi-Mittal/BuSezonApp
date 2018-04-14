package com.example.mansi.busezon.data;


import android.provider.BaseColumns;

public final class dbContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private dbContract() {}

    /**
     * Inner class that defines constant values for the pets database table.
     * Each entry in the table represents a single pet.
     */
    public static final class userEntry implements BaseColumns {

        /** Name of database table for pets */
        public final static String TABLE_NAME = "cust";

        /**
         * Unique ID number for the pet (only for use in the database table).
         *
         * Type: INTEGER
         */
       // public final static String _ID = BaseColumns._ID;

       // public final static String userID = "ID";

        public final static String COLUMN_USER_ID = "user_id";

        public final static String COLUMN_USER = "name";


        public final static String COLUMN_EMAIL = "EMAIL";


        public final static String COLUMN_number = "number";

        public final static String COLUMN_password = "password";


        public final static String COLUMN_address = "Address";


    }
}


