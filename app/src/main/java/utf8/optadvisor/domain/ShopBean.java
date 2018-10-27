package utf8.optadvisor.domain;

import java.util.List;

public class ShopBean {

    private int shopId;
    private String shopName;
    private List<GoodsBean> cartlist;

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<GoodsBean> getCartlist() {
        return cartlist;
    }

    public void setCartlist(List<GoodsBean> cartlist) {
        this.cartlist = cartlist;
    }

    public void addGoods(GoodsBean goodsBean){
        this.cartlist.add(goodsBean);
    }

    public static class GoodsBean {
        private int id;
        private int shopId;
        private String shopName;
        private int proId;
        private String proName;
        private String info;
        private double price;
        private int count;
        private String defaultPic;
        private boolean isSelect=true;
        private int isFirst=2;
        private boolean isShopSelect=true;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getShopId() {
            return shopId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public int getProId() {
            return proId;
        }

        public void setProId(int proId) {
            this.proId = proId;
        }

        public String getProName() {
            return proName;
        }

        public void setProName(String proName) {
            this.proName = proName;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getDefaultPic() {
            return defaultPic;
        }

        public void setDefaultPic(String defaultPic) {
            this.defaultPic = defaultPic;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public int getIsFirst() {
            return isFirst;
        }

        public void setIsFirst(int isFirst) {
            this.isFirst = isFirst;
        }

        public boolean isShopSelect() {
            return isShopSelect;
        }

        public void setShopSelect(boolean shopSelect) {
            isShopSelect = shopSelect;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}
