package lk.ijse.dep.entity;

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

    public CustomEntity(String orderId, String customerName, Date orderDate) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.orderDate = orderDate;
    }

    public CustomEntity(String customerId, String customerName, String orderId) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerId = customerId;
    }

}
