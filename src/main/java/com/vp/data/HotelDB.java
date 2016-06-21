package com.vp.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author vijay
 * This Class parses the hotelDB.csv and keep and in memory index of hotels.
 * indexing is done considering two api request i.e individual hotel id request and city based request. 
 */
public class HotelDB {
    public static final String WHITE_SPACES = "\\s+";
    private static final Log log = LogFactory.getLog(HotelDB.class);
    private static HotelDB instance;
    private List<Hotel> hotels;
    private Map<String, List<Hotel>> cityHotelIndex;

    private HotelDB() {
        hotels = new ArrayList<Hotel>();
        cityHotelIndex = new HashMap<String, List<Hotel>>();
    }

    public static HotelDB getInstance() {
        if (instance == null) {
            instance = new HotelDB();
            instance.loadFromCsv();
        }
        return instance;
    }

    protected void loadFromCsv() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("hoteldb.csv");
        try {
            String csvData = IOUtils.toString(inputStream);
            String[] rows = csvData.split("\r");
            for (int i = 1; i < rows.length; i++) {
                String[] column = rows[i].split(",");
                Hotel hotel = new Hotel();
                String city = column[0].toLowerCase().replaceAll(WHITE_SPACES, "");
                List<Hotel> cityHotel = null;
                if (cityHotelIndex.containsKey(city)) {
                    cityHotel = cityHotelIndex.get(city);
                } else {
                    cityHotel = new ArrayList<Hotel>();
                    cityHotelIndex.put(city, cityHotel);
                }
                hotel.setCity(column[0]);
                hotel.setId(Integer.parseInt(column[1]));
                Room room = new Room();
                room.setType(column[2]);
                room.setPrice(Double.parseDouble(column[3]));
                hotel.setRoom(room);
                hotels.add(hotel);
                cityHotel.add(hotel);
            }
        } catch (IOException e) {
            log.error("Error parsing hotel db");
        }
        log.info(cityHotelIndex);
    }

    public List<Hotel> getCityHotels(String city) {
        String key = city.replaceAll(WHITE_SPACES, "").toLowerCase();
        return cityHotelIndex.get(key);
    }

    public Hotel getHotel(int id) {
        Hotel hotel = hotels.stream().filter(h -> h.getId() == id).findFirst().orElse(null);
        return hotel;
    }

}
