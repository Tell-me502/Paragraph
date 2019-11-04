package com.example.liyun.paragraph;

import java.util.List;

/**
 * 返回内容response
 */
public class FunsApi_translation {
        private int error_code;
        private String reason;
        private Resultfuns result;
    public static class Resultfuns {
        private List<Datas> data;

        public List<Datas> getDatas() {
            return data;
        }

        public void setDatas(List<Datas> datas) {
            this.data = data;
        }
    }
    public static class Datas{
        private String content;
        private String hashId;
        private String updatetime;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getHashId() {
            return hashId;
        }

        public void setHashId(String hashId) {
            this.hashId = hashId;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        @Override
        public String toString() {
            return "Datas{" +
                    "content='" + content + '\'' +
                    ", hashId='" + hashId + '\'' +
                    ", updatetime='" + updatetime + '\'' +
                    '}';
        }
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Resultfuns getResult() {
        return result;
    }

    public void setResult(Resultfuns result) {
        this.result = result;
    }



    @Override
    public String toString() {
        return "FunsApi_translation{" +
                "error_code=" + error_code +
                ", reason='" + reason + '\'' +
                ", result=" + result.data +
                '}';
    }
}
