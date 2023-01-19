module net.safefleet.prod.productionscheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;

    opens net.safefleet.prod.productionscheduler to javafx.fxml;
    exports net.safefleet.prod.productionscheduler;
    opens net.safefleet.prod.productionscheduler.data to javafx.fxml;
    exports net.safefleet.prod.productionscheduler.data;
    exports net.safefleet.prod.productionscheduler.controllers;
    opens net.safefleet.prod.productionscheduler.controllers to javafx.fxml;

}