package lk.ijse.dep.controller;

import lk.ijse.dep.AppInitializer;
import lk.ijse.dep.business.custom.CustomerBO;
import lk.ijse.dep.business.custom.ItemBO;
import lk.ijse.dep.business.custom.OrderBO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dep.util.*;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;

public class PlaceOrderFormController {
    static ArrayList<Order> ordersDB = new ArrayList<>();

    public JFXTextField txtDescription;
    public JFXTextField txtCustomerName;
    public JFXTextField txtQtyOnHand;
    public JFXButton btnSave;
    public JFXTextField txtUnitPrice;
    public JFXComboBox<CustomerTM> cmbCustomerId;
    public JFXComboBox<ItemTM> cmbItemCode;
    public JFXTextField txtQty;
    public Label lblTotal;
    public JFXButton btnPlaceOrder;
    public AnchorPane root;
    public Label lblId;
    public Label lblDate;
    public TableView<OrderDetailTM> tblOrderDetails;
    private boolean readOnly;

    private OrderBO orderBO = AppInitializer.getCtx().getBean(OrderBO.class);
    private ItemBO itemBO =AppInitializer.getCtx().getBean(ItemBO.class);
    private CustomerBO customerBO =AppInitializer.getCtx().getBean(CustomerBO.class);

    public void initialize() {

        readOnly = false;

        // Basic initializations
        txtCustomerName.setEditable(false);
        txtQtyOnHand.setEditable(false);
        txtUnitPrice.setEditable(false);
        txtDescription.setEditable(false);

        // Let's set the date
        LocalDate today = LocalDate.now();
        lblDate.setText(today.toString());

        // Let's load all the customer ids
        loadAllCustomers();

        // When user selects a customer id
        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CustomerTM>() {
            @Override
            public void changed(ObservableValue<? extends CustomerTM> observable, CustomerTM oldValue, CustomerTM newValue) {
                if (newValue == null) {
                    txtCustomerName.clear();
                    return;
                }
                txtCustomerName.setText(newValue.getName());
            }
        });

        // Let's load all the item codes
        loadAllItems();

        // When user selects a item code
        cmbItemCode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ItemTM>() {
            @Override
            public void changed(ObservableValue<? extends ItemTM> observable, ItemTM oldValue, ItemTM newValue) {

                if (newValue == null) {
                    txtUnitPrice.clear();
                    txtDescription.clear();
                    txtQtyOnHand.clear();
                    btnSave.setDisable(true);
                    return;
                }

                btnSave.setDisable(false);
                txtDescription.setText(newValue.getDescription());
                calculateQtyOnHand(newValue);
                txtUnitPrice.setText(newValue.getUnitPrice() + "");
            }
        });

        // Let's map columns
        tblOrderDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("code"));
        tblOrderDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblOrderDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblOrderDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblOrderDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));
        tblOrderDetails.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("button"));

        btnSave.setDisable(true);

        tblOrderDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OrderDetailTM>() {
            @Override
            public void changed(ObservableValue<? extends OrderDetailTM> observable, OrderDetailTM oldValue, OrderDetailTM selectedOrderDetail) {

                if (selectedOrderDetail == null) {
                    return;
                }

                String selectedItemCode = selectedOrderDetail.getCode();
                ObservableList<ItemTM> items = cmbItemCode.getItems();
                for (ItemTM item : items) {
                    if (item.getCode().equals(selectedItemCode)) {
                        cmbItemCode.getSelectionModel().select(item);
                        txtQtyOnHand.setText(item.getQtyOnHand() + "");
                        txtQty.setText(selectedOrderDetail.getQty() + "");
                        if (!readOnly) {
                            btnSave.setText("Update");
                        }
                        if (readOnly) {
                            txtQty.setDisable(true);
                            btnSave.setDisable(true);
                        }
                        cmbItemCode.setDisable(true);
                        Platform.runLater(() -> {
                            txtQty.requestFocus();
                        });
                        break;
                    }
                }
            }
        });

        generateOrderId();
    }

    private void loadAllItems() {
        cmbItemCode.getItems().clear();
        try {
            cmbItemCode.setItems(FXCollections.observableArrayList(itemBO.getAllItems()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAllCustomers() {
        cmbCustomerId.getItems().clear();
        try {
            cmbCustomerId.setItems(FXCollections.observableArrayList(customerBO.getAllCustomers()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateQtyOnHand(ItemTM item) {
        txtQtyOnHand.setText(item.getQtyOnHand() + "");
        ObservableList<OrderDetailTM> orderDetails = tblOrderDetails.getItems();
        for (OrderDetailTM orderDetail : orderDetails) {
            if (orderDetail.getCode().equals(item.getCode())) {
                int displayQty = item.getQtyOnHand() - orderDetail.getQty();
                txtQtyOnHand.setText(displayQty + "");
                break;
            }
        }
    }



    public void btnAdd_OnAction(ActionEvent actionEvent) {
        // Let's do some validation
        if (txtQty.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Qty can't be empty", ButtonType.OK).show();
            return;
        }
        int qty = Integer.parseInt(txtQty.getText());
        if (qty < 1 || qty > Integer.parseInt(txtQtyOnHand.getText())) {
            new Alert(Alert.AlertType.ERROR, "Invalid Qty.", ButtonType.OK).show();
            return;
        }

        ItemTM selectedItem = cmbItemCode.getSelectionModel().getSelectedItem();
        ObservableList<OrderDetailTM> orderDetails = tblOrderDetails.getItems();

        if (btnSave.getText().equals("Add")) {
            boolean exist = false;
            for (OrderDetailTM orderDetail : orderDetails) {
                if (orderDetail.getCode().equals(selectedItem.getCode())) {
                    exist = true;
                    orderDetail.setQty(orderDetail.getQty() + qty);
                    tblOrderDetails.refresh();
                    break;
                }
            }

            if (!exist) {
                Button btnDelete = new Button("Delete");
                OrderDetailTM orderDetail = new OrderDetailTM(selectedItem.getCode(),
                        selectedItem.getDescription(),
                        qty,
                        selectedItem.getUnitPrice(), qty * selectedItem.getUnitPrice(), btnDelete);
                btnDelete.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        tblOrderDetails.getSelectionModel().clearSelection();
                        cmbItemCode.getSelectionModel().clearSelection();
                        cmbItemCode.setDisable(false);
                        txtQty.clear();
                        orderDetails.remove(orderDetail);
                        cmbItemCode.requestFocus();
                    }
                });
                orderDetails.add(orderDetail);
            }

            calculateTotal();
            cmbItemCode.getSelectionModel().clearSelection();
            txtQty.clear();
            cmbItemCode.requestFocus();
        } else {
            // Update
            OrderDetailTM selectedOrderDetail = tblOrderDetails.getSelectionModel().getSelectedItem();
            selectedOrderDetail.setQty(qty);
            selectedOrderDetail.setTotal(qty * selectedOrderDetail.getUnitPrice());
            tblOrderDetails.refresh();

            tblOrderDetails.getSelectionModel().clearSelection();
            btnSave.setText("Add");
            cmbItemCode.setDisable(false);
            cmbItemCode.getSelectionModel().clearSelection();
            txtQty.clear();
            calculateTotal();
            cmbItemCode.requestFocus();
        }
    }

    public void btnPlaceOrder_OnAction(ActionEvent actionEvent) {
        // Validation
        if (cmbCustomerId.getSelectionModel().getSelectedIndex() == -1) {
            new Alert(Alert.AlertType.ERROR, "You need to select a customer", ButtonType.OK).show();
            cmbCustomerId.requestFocus();
            return;
        }

        if (tblOrderDetails.getItems().size() == 0) {
            new Alert(Alert.AlertType.ERROR, "Ubata pissuda yako, nikan order dannea", ButtonType.OK).show();
            cmbItemCode.requestFocus();
            return;
        }


        try {
          orderBO.placeOrder(new OrderTM(lblId.getText(), LocalDate.now(), cmbCustomerId.getValue().getId(), cmbCustomerId.getValue().getName(),0),tblOrderDetails.getItems());
            new Alert(Alert.AlertType.INFORMATION, "Mudalali wade goda", ButtonType.OK).showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Mudalali wade awul wage", ButtonType.OK).show();

        }


        tblOrderDetails.getItems().clear();
        txtQty.clear();
        cmbCustomerId.getSelectionModel().clearSelection();
        cmbItemCode.getSelectionModel().clearSelection();
        calculateTotal();
        generateOrderId();
    }
    @FXML
    private void navigateToHome(MouseEvent event) throws IOException {
        URL resource = this.getClass().getResource("/lk/ijse/dep/view/MainForm.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    public void txtQty_OnAction(ActionEvent actionEvent) {
        btnAdd_OnAction(actionEvent);
    }

    public void calculateTotal() {
        ObservableList<OrderDetailTM> orderDetails = tblOrderDetails.getItems();
        double netTotal = 0;
        for (OrderDetailTM orderDetail : orderDetails) {
            netTotal += orderDetail.getTotal();
        }
        NumberFormat numberInstance = NumberFormat.getNumberInstance();
        numberInstance.setMaximumFractionDigits(2);
        numberInstance.setMinimumFractionDigits(2);
        numberInstance.setGroupingUsed(false);
        String formattedText = numberInstance.format(netTotal);
        lblTotal.setText("Total: " + formattedText);
    }

    private void generateOrderId() {
        // Generate a new id
        try {
            lblId.setText(orderBO.getNewOrderId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}
