package com.islamic.khatmah.free_reading;

public class SurahClass {

        private int page_number;
        private String number;
        private String name;
        private String type;
        private String number_of_verses;

        public SurahClass(String number, String name, String type, String number_of_verses) {
            this.number = number;
            this.name = name;
            this.type = type;
            this.number_of_verses = number_of_verses;
        }

        public SurahClass(String number, String name, String type) {
            this.number = number;
            this.name = name;
            this.type = type;
        }

    public SurahClass(int page_number, String number, String name, String type, String number_of_verses) {
        this.page_number = page_number;
        this.number = number;
        this.name = name;
        this.type = type;
        this.number_of_verses = number_of_verses;
    }

    public int getPage_number() {
        return page_number;
    }

    public void setPage_number(int page_number) {
        this.page_number = page_number;
    }

    public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNumber_of_verses() {
            return number_of_verses;
        }

        public void setNumber_of_verses(String number_of_verses) {
            this.number_of_verses = number_of_verses;
        }
    }
