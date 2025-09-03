package com.ttknp.springbootandjdbcintermediaryservice.helpers.sql_order_by.entity;

import java.util.List;

public class RequestOrderBy {

    private Integer length;
    private List<OrderBy> orderBy;

    public RequestOrderBy(Integer length, List<OrderBy> orderBy) {
        this.length = length;
        this.orderBy = orderBy;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public List<OrderBy> getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(List<OrderBy> orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderBy(String alias, List<OrderBy> orderBy) {
        StringBuilder stringBuilder = new StringBuilder();
        if (orderBy.size() == 1) { // case one order by
            OrderBy orderByItem = orderBy.get(0); // get order as object
            compactColumnAndDirection(stringBuilder, alias, orderByItem); // after compactColumnAndDirection(...) done stringBuilder changed
        }
        else { // case many order by
            for (int i = 0; i < orderBy.size(); i++) {
                OrderBy orderByItem = orderBy.get(i);
                compactColumnAndDirection(stringBuilder, alias, orderByItem);
                if (i + 1 < orderBy.size()) {  // add comma if not last item
                    stringBuilder.append(",");
                }
            }
        }
        if (length != null) {
            stringBuilder
                    .append(" limit ")
                    .append(length);
        }
        return stringBuilder.toString();
    }

    private void compactColumnAndDirection(StringBuilder stringBuilder, String alias, OrderBy orderBy) {
        stringBuilder.append(" ")
                .append(alias)
                .append(".")
                .append(orderBy.getColumn())
                .append(" ")
                .append(orderBy.getDirection());
    }

    public static class OrderBy {

        private String column;
        private String direction; // ASC or DESC

        public OrderBy(String column, String direction) {
            this.column = column;
            this.direction = direction;
        }

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("OrderBy{");
            sb.append("column='").append(column).append('\'');
            sb.append(", direction='").append(direction).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

}
