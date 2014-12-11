package ee.ut.logreader;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.text.Text;

public class Connection {

	public Connection(Server server) {
		super();
		ConnectionWindow(server);

	}

	public void ConnectionWindow(Server server) {

		StackPane ConnectionRoot = new StackPane();

		Stage ConnectionStage = new Stage();
		ConnectionStage.setTitle("Serveri sisu");

		VBox vBox = new VBox(10);
		vBox.setAlignment(Pos.CENTER);

		ComboBox LogiFailid = new ComboBox();
		List<String> Logs = ((ServerCommands) server).getLogFiles();
		LogiFailid.getItems().addAll(Logs);
		LogiFailid.setPromptText("Vali logifail, mille sisu tahad lugeda");

		Text searchlabel = new Text();
		searchlabel
				.setText("Või sisesta sõne, mille järgi teostatakse otsing kõigites serverites");

		Button searchButton = new Button("Otsi");

		TextField search = new TextField();
		search.setPromptText("Otsi kõikides logides sõne järgi");
		TextArea result = new TextArea();
		result.setPromptText("Tulemus");

		vBox.getChildren().addAll(LogiFailid, searchlabel, search,
				searchButton, result);
		ConnectionRoot.getChildren().add(vBox);
		Scene stseen1 = new Scene(ConnectionRoot, 1000, 500, Color.WHEAT);
		ConnectionStage.setResizable(true);
		ConnectionStage.setScene(stseen1);
		ConnectionStage.show();

		result.setPrefSize(300, 400);

		// Teostab otsingu kliki peale
		searchButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent me) {
				String seachString = search.getText();
				if (seachString.length() > 0) {
					((ServerCommands) server).grepAllByKeyword(seachString,
							result);

				}

			}
		});

		// Teine variant otsingust, enteri peale
		search.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER) {
					String seachString = search.getText();
					if (seachString.length() > 0) {
						((ServerCommands) server).grepAllByKeyword(seachString,
								result);

					}
				}
			}
		});
		
		LogiFailid.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String t, String t1) {
				((ServerCommands) server).readChosenFile(t1, result);
			}
		});

		// paneb kinni serveri akna kinnipanemisel
		ConnectionStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				server.Close();
			}
		});

	}

}
