package lk.ijse.dep.entity;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
//@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "orderDetailList")
@Table(name = "`Order`")
public class Order implements SuperEntity {

  @Id
  private String id;
  private Date date;
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
  @JoinColumn(name = "customerId", referencedColumnName = "id", nullable = false)
  private Customer customerId;
  //by directional
  @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
  private List<OrderDetail> orderDetailList;

  public Order(String id, Date date, Customer customerId) {
    this.id = id;
    this.date = date;
    this.customerId = customerId;
  }

  public Order(String id, Date date, Customer customerId, List<OrderDetail> orderDetailList) {
    this.id = id;
    this.date = date;
    this.customerId = customerId;
    for (OrderDetail orderDetail : orderDetailList) {
      orderDetail.setOrder(this);
    }
    this.orderDetailList = orderDetailList;
  }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
    for (OrderDetail orderDetail : orderDetailList) {
      orderDetail.setOrder(this);
    }
    this.orderDetailList = orderDetailList;
  }

  public void addOrderDetail(OrderDetail orderDetail) {
    orderDetail.setOrder(this);
    this.getOrderDetailList().add(orderDetail);
  }

}
