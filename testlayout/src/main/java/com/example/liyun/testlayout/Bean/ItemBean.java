package com.example.liyun.testlayout.Bean;

import com.google.gson.JsonElement;

/**
 * 模块文章下实体类
 */
public class ItemBean {
        /**
         * image : https://imgsa.baidu.com/news/q%3D100/sign=06d12a2732dbb6fd235be1263925aba6/b151f8198618367a8dbfe10125738bd4b31ce53c.jpg
         * url : http://news.ifeng.com/a/20180120/55302258_0.shtml?_zbs_baidu_news
         * title : 安徽合肥：“非遗”进校园
         * id : whyidzhi
         * order : 1
         */

        private String image;
        private String url;
        private String title;
        private String id;
        private String order;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

}
