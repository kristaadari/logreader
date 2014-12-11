package ee.ut.logreader;

import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Layout extends Application {

	public void start(Stage stage1) {

		StackPane root = new StackPane();

		//lisame tervitusteksti
		Label welcomelabel = new Label();
		welcomelabel
				.setText("Tere tulemast! Programm aitab uurida logifaile etteantud serveris");

		root.getChildren().add(welcomelabel);
		StackPane.setAlignment(welcomelabel, Pos.TOP_CENTER);

		//errorteksti kujunduse loomine
		Text ErrorMessage = new Text();
		ErrorMessage.setFont(Font.font("Serif", 15));
		ErrorMessage.setFill(Color.RED);

		//salvestatud serverite loomine, nähtavad ainult juhul, kui on mõni olemas
		ComboBox salvestatudServerid = new ComboBox();
		salvestatudServerid.setVisible(false);
		LoadSavedServersToCombo(salvestatudServerid);

		VBox vBox = new VBox(10);
		vBox.setAlignment(Pos.CENTER);

		//tekstikastide ja nende jaoks siltide loomine
		Text ipQ = new Text();
		ipQ.setText("Serveri IP");
		ipQ.setFont(Font.font("Serif", 15));

		TextField ip = new TextField();
		ip.setPromptText("Kirjuta siia serveri IP.");

		Text usernameQ = new Text();
		usernameQ.setText("Kasutajatunnus");
		usernameQ.setFont(Font.font("Serif", 15));

		TextField username = new TextField();
		username.setPromptText("Kirjuta siia oma kasutajanimi.");

		Text passwordQ = new Text();
		passwordQ.setText("Parool");
		passwordQ.setFont(Font.font("Serif", 15));

		PasswordField password = new PasswordField();
		password.setPromptText("Kirjuta siia oma parool.");

		// nuppude paigutamine
		Button submit = new Button("Logi sisse");
		CheckBox saveB = new CheckBox("Salvesta");

		FlowPane pane = new FlowPane(10, 10);
		pane.setAlignment(Pos.CENTER);
		pane.getChildren().addAll(submit, saveB);

		//kuulaja loomine, et teha nähtavaks salvestatud serverid
		salvestatudServerid.valueProperty().addListener(
				new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue ov, String t, String t1) {
						ip.setText(t1);
						FillLoginFromCombo(t1, password, username);
					}
				});

		//andmete saatmise kuulamine nupulevajutusel
		submit.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {

				ErrorMessage.setText("");

				String LoginIP = ip.getText();
				String LoginUsername = username.getText();
				String LoginPassword = password.getText();

				//kontroll, kas mõni väli jäi tühjaks või ühendust ei saanud
				//kui jah, siis sobivale kohale kuvatakse sobiv error
				if (LoginIP.length() > 0) {
					if (LoginUsername.length() > 0) {
						if (LoginPassword.length() > 0) {

							Server server = new ServerCommands(LoginIP,
									LoginUsername, LoginPassword);

							if (server.SSHConnection()) {
								Connection connection = new Connection(server);
								// Salvestab kasutaja andmeid ainult siis kui
								// ühendus õnnestus
								if (saveB.isSelected()) {
									String[] userData = new String[] { LoginIP,
											LoginUsername, LoginPassword };
									GetUserData.saveData(userData);
									LoadSavedServersToCombo(salvestatudServerid);

								}

							}

							else
								ErrorMessage
										.setText("Ühendus serveriga ebaõnnestus. Kontrolli sisestatud andmete õigsust!");

						} else
							ErrorMessage.setText("Sisesta parool");
					} else
						ErrorMessage.setText("Sisesta kasutajatunnus");
				} else
					ErrorMessage.setText("Sisesta õige host");

			}
		});

		//elementide lisamine
		vBox.getChildren().addAll(ErrorMessage, ipQ, ip, usernameQ, username,
				passwordQ, password, pane, salvestatudServerid);
		root.getChildren().add(vBox);

		//stseeni kujunduse sättimine
		Scene stseen1 = new Scene(root, 500, 300, Color.WHEAT);
		
		//lava tiitel ja suuruse muutmise võimalus, nätavaks tegemine
		stage1.setTitle("LogideUurija");
		stage1.setResizable(true);
		stage1.setScene(stseen1);
		stage1.show();

	}

	//meetod salvestatud serverite lugemiseks
	public void LoadSavedServersToCombo(ComboBox combo) {
		List<String> savedData = GetUserData.existingData();
		if (savedData.size() > 0) {
			for (int i = 0; i < savedData.size(); i++) {
				if (savedData.get(i).equals("---server")) {
					combo.getItems().add(savedData.get(i + 1));

				}

			}
			combo.setPromptText("Vali salvestatud server");
			combo.setVisible(true);
		}

	}

	//meetod väljade täitmiseks valitud serveriandmetega
	public void FillLoginFromCombo(String chosenServer, TextField pass,
			TextField username) {
		List<String> savedData = GetUserData.existingData();
		username.setText(savedData.get(savedData.indexOf(chosenServer) + 1));
		pass.setText(savedData.get(savedData.indexOf(chosenServer) + 2));
	}

	public static void main(String[] args) {
		launch(args);
	}

}
