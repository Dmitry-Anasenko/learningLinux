module ru.hiik.learninglinux {
    requires javafx.controls;
    requires javafx.web;
    requires javafx.fxml;
    requires java.base;
    requires org.commonmark;

    opens ru.hiik.learninglinux to javafx.fxml;
    exports ru.hiik.learninglinux;
}
