package com.example.xiaojingw.model.doment;

import java.io.Serializable;

public class TicketResult implements Serializable {


    private boolean success;
    private int code;
    private String message;
    private DataDTOX data;

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

    public DataDTOX getData() {
        return data;
    }

    public void setData(DataDTOX data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TicketResult{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataDTOX implements Serializable {
        private TbkTpwdCreateResponseDTO tbk_tpwd_create_response;

        public TbkTpwdCreateResponseDTO getTbk_tpwd_create_response() {
            return tbk_tpwd_create_response;
        }

        public void setTbk_tpwd_create_response(TbkTpwdCreateResponseDTO tbk_tpwd_create_response) {
            this.tbk_tpwd_create_response = tbk_tpwd_create_response;
        }

        public static class TbkTpwdCreateResponseDTO implements Serializable {
            private DataDTO data;
            private String request_id;

            @Override
            public String toString() {
                return "TbkTpwdCreateResponseDTO{" +
                        "data=" + data +
                        ", request_id='" + request_id + '\'' +
                        '}';
            }

            public DataDTO getData() {
                return data;
            }

            public void setData(DataDTO data) {
                this.data = data;
            }

            public String getRequest_id() {
                return request_id;
            }

            public void setRequest_id(String request_id) {
                this.request_id = request_id;
            }

            public static class DataDTO implements Serializable {
                private String model;

                @Override
                public String toString() {
                    return "DataDTO{" +
                            "model='" + model + '\'' +
                            '}';
                }

                public String getModel() {
                    return model;
                }

                public void setModel(String model) {
                    this.model = model;
                }
            }
        }
    }
}
