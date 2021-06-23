package com.example.xiaojingw.model.doment;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

public class SelectedPageCategory implements Serializable {
    @Override
    public String toString() {
        return "SelectedPageCategory{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    private boolean success;
    private int code;
    private String message;
    private List<DataDTO> data;

    public static SelectedPageCategory objectFromData(String str) {

        return new Gson().fromJson(str, SelectedPageCategory.class);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public static class DataDTO implements Serializable {
        @Override
        public String toString() {
            return "DataDTO{" +
                    "type=" + type +
                    ", favorites_id=" + favorites_id +
                    ", favorites_title='" + favorites_title + '\'' +
                    '}';
        }

        private int type;
        private int favorites_id;
        private String favorites_title;

        public static DataDTO objectFromData(String str) {

            return new Gson().fromJson(str, DataDTO.class);
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getFavorites_id() {
            return favorites_id;
        }

        public void setFavorites_id(int favorites_id) {
            this.favorites_id = favorites_id;
        }

        public String getFavorites_title() {
            return favorites_title;
        }

        public void setFavorites_title(String favorites_title) {
            this.favorites_title = favorites_title;
        }
    }
}
