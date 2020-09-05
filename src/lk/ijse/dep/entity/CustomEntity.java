package lk.ijse.dep.entity;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomEntity implements SuperEntity {
    private String orderId;
    private String customerName;
    private Date orderDate;
    private String customerId;
    private BigDecimal total;

    public CustomEntity(String orderId, Date orderDate, String customerId, String customerName, BigDecimal total) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.customerName = customerName;
        this.customerId = customerId;
        this.total = total;
    }

}
