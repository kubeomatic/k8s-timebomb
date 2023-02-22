package io.kubeomatic.service;

import java.util.*;

public class Epoch {
    public static Long dateToEpoch(Date date){
        /**
         * Convert Date do EPOCH
         * https://www.epoch101.com/Java
         */
        long epoch = date.getTime() / 1000;
        return epoch;
    }
    public static Long dateToEpoch(){
        /**
         * Return NOW in epoch
         */
        Date date = new Date();
        long epoch = date.getTime() / 1000;
        return epoch;
    }
    public static Date epochToDate(Long epoch){
        /**
         * Convert EPOCH to Date
         * https://www.epoch101.com/Java
         */
        Date date = new Date( epoch * 1000 );
        return date;
    }
}
