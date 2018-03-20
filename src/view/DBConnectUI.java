package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.awt.event.ActionEvent;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import model.DBConnection;

import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class DBConnectUI extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField hostTxt;
	private JTextField portTxt;
	private JTextField dbNameTxt;
	private JTextField usernameTxt;
	private JTextField passwordTxt;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DBConnectUI dialog = new DBConnectUI();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DBConnectUI() {
		setBounds(100, 100, 391, 282);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		{
			JLabel lblDatabaseSettings = new JLabel("Database Settings");
			contentPanel.add(lblDatabaseSettings, "2, 2");
		}
		{
			JLabel lblHost = new JLabel("Host");
			contentPanel.add(lblHost, "2, 6");
		}
		{
			hostTxt = new JTextField();
			contentPanel.add(hostTxt, "4, 6, 9, 1, fill, default");
			hostTxt.setColumns(10);
		}
		{
			JLabel lblPort = new JLabel("Port");
			contentPanel.add(lblPort, "2, 8");
		}
		{
			portTxt = new JTextField();
			contentPanel.add(portTxt, "4, 8, 9, 1, fill, default");
			portTxt.setColumns(10);
		}
		{
			JLabel lblDatabaseName = new JLabel("Database name");
			contentPanel.add(lblDatabaseName, "2, 10");
		}
		{
			dbNameTxt = new JTextField();
			contentPanel.add(dbNameTxt, "4, 10, 9, 1, fill, default");
			dbNameTxt.setColumns(10);
		}
		{
			JLabel lblUsername = new JLabel("Username");
			contentPanel.add(lblUsername, "2, 12");
		}
		{
			usernameTxt = new JTextField();
			contentPanel.add(usernameTxt, "4, 12, 9, 1, fill, default");
			usernameTxt.setColumns(10);
		}
		{
			JLabel lblPassword = new JLabel("Password");
			contentPanel.add(lblPassword, "2, 14");
		}
		{
			passwordTxt = new JPasswordField();
			contentPanel.add(passwordTxt, "4, 14, 9, 1, fill, default");
			passwordTxt.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Connect");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						DBConnection db = new DBConnection(hostTxt.getText(), 
								portTxt.getText(), 
								dbNameTxt.getText(), 
								usernameTxt.getText(), 
								passwordTxt.getText());
						Connection con = db.getConnection();
						if (con != null) {
							try {
								hw3 frame = new hw3();
								frame.setVisible(true);
								dispose();
							} catch (Exception e2) {
								e2.printStackTrace();
							}
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
