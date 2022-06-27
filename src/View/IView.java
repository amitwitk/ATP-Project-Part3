package View;

import ViewModel.MyViewModel;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public interface IView {

    void initialize(URL url, ResourceBundle resourceBundle);

    void set_Resize(Scene scene);

    void setViewModel(MyViewModel viewModel);

    void setStage(Stage pstage);
}
