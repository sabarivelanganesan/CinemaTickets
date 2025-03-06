module uk.gov.dwp.uc.pairtest {
    requires javafx.controls;
    requires javafx.fxml;

    opens uk.gov.dwp.uc.pairtest to javafx.fxml;
    exports uk.gov.dwp.uc.pairtest;
    exports uk.gov.dwp.uc.pairtest.exception;
    exports uk.gov.dwp.uc.pairtest.domain;
}
