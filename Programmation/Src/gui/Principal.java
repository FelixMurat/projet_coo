package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import controller.Controller;
import model.Model;
import model.MsgUser;
import model.User;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class Principal extends JFrame {

	private JPanel contentPane;
	private Controller controller;
	private Model model;
	private JLabel activeUser;
	private JPanel chatPanel;
	private Connexion connexion;
	private JTextField messageField;
	JScrollPane messagesScroll;
	JList<String> messages_list;
	DefaultListModel<String> messages_string;
	private User activeuser;
	JList<String> connected_list;
	DefaultListModel<String> connected_string;

	/**
	 * Create the frame.
	 */
	public Principal(Controller controller, Model model, Connexion connexion) {

		this.controller = controller;
		this.model = model;
		this.connexion = connexion;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 528, 530);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

//		String[] test_user = { "test1", "test2", "test3", "test4", "test5", "test6", "test7", "test2", "test3", "test4",
//				"test5", "test6", "test7", "test2", "test3", "test4", "test5", "test6", "test7", "test2", "test3",
//				"test4", "test5", "test6", "test7", "test2", "test3", "test4", "test5", "test6", "test7", "test2",
//				"test3", "test4", "test5", "test6", "test7", "test2", "test3", "test4", "test5", "test6", "test7",
//				"test2", "test3", "test4", "test5", "test6", "test7", "test2", "test3", "test4", "test5", "test6",
//				"test7" };
		InitializeConnected();
		connected_string = new DefaultListModel<>();
		connected_string.addElement("toto1");
		connected_list = new JList<>(connected_string);
		connected_list.addListSelectionListener((ListSelectionListener) new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent event) {
				if (!event.getValueIsAdjusting()) {
					JList source = (JList) event.getSource();
					String selected = source.getSelectedValue().toString();
					System.out.println(selected);
					ArrayList<User> List = model.connectedUserList;
					for (int i = 0; i < List.size(); i++) {
						if (List.get(i).getPseudo().equals(selected)) {
							activeuser = List.get(i);
						}
					}
					activeUser.setText(selected);
					chatPanel.setVisible(true);
					// activeUser.setText(selected)
					// Envoyer le message Via TCP
					User userSender = new User(2, "test", "test");
					User userReceiver = new User(1, "test", "test");
					// MsgUser msg = new MsgUser("oui et toi ?");
					// controller.database.SaveMsg(userSender, userReceiver, msg);
					List<MsgUser> allMessages = controller.database.GetHistory(userSender, userReceiver);
					InitMessages(allMessages);
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(connected_list);
		scrollPane.setBounds(344, 47, 170, 360);
		contentPane.add(scrollPane);

		chatPanel = new JPanel();
		chatPanel.setBounds(12, 47, 332, 405);
		contentPane.add(chatPanel);
		chatPanel.setLayout(null);
		
		messages_string = new DefaultListModel<>();
		messages_list = new JList<>(messages_string);
		messagesScroll = new JScrollPane(messages_list);
		messagesScroll.setBounds(0, 39, 320, 299);
		chatPanel.add(messagesScroll);

		activeUser = new JLabel("lol");
		activeUser.setBounds(12, 12, 70, 15);
		chatPanel.add(activeUser);

		JButton reduceButon = new JButton("Reduce");
		reduceButon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		reduceButon.setBounds(234, 1, 86, 37);
		chatPanel.add(reduceButon);

		JButton deletButoon = new JButton("Delet");
		deletButoon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chatPanel.setVisible(false);
				activeUser.setText("lol");
			}
		});
		deletButoon.setBounds(152, 1, 86, 37);
		chatPanel.add(deletButoon);

		messageField = new JTextField();
		messageField.setBounds(10, 350, 248, 43);
		chatPanel.add(messageField);
		messageField.setColumns(10);

		JButton sendmessageButton = new JButton(">");
		sendmessageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
			    Date date = new Date();
				String msg = "moi: " + messageField.getText() + " ( "+ formatter.format(date)+ " ) ";
				messages_string.addElement(msg);
			}
		});
		sendmessageButton.setBounds(267, 350, 53, 43);
		chatPanel.add(sendmessageButton);

		JButton disconnectButton = new JButton("Disconnect");
		disconnectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FermerFenetre();
				getConnexion().comebackPrincipal();
			}
		});
		disconnectButton.setBounds(12, 0, 117, 35);
		contentPane.add(disconnectButton);

		JButton changepseudoButton = new JButton("Change Pseudo");
		changepseudoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// appel de fonction de changement de pseudo
			}
		});
		changepseudoButton.setBounds(134, 0, 145, 35);
		contentPane.add(changepseudoButton);
		chatPanel.setVisible(false);

	}

	public Connexion getConnexion() {
		return connexion;
	}

	private void InitializeConnected() {
		ArrayList<User> List = model.connectedUserList;
		for (int i = 0; i < List.size(); i++) {
			connected_string.addElement(List.get(i).getPseudo());
		}
	}

	private void InitMessages(List<MsgUser> allMessages) {
		String currentString;
		for (int i = 0 ; i < allMessages.size() ; i++)
		{
			MsgUser msg = allMessages.get(i);
			if (msg.getIdSender() == model.getCurrentUser().getUserID())
			{
				currentString = "moi: " + msg.getContent() + " ( " + msg.getDate() + " )";
			}
			else
			{
				currentString = "autre "  +": " + msg.getContent() + " ( " + msg.getDate() + " )";
			}
			messages_string.addElement(currentString);
		}
	}

	public void addConnecteduser(User user) {
		connected_string.addElement(user.getPseudo());
	}

	public void removeConnecteduser(User user) {
		connected_string.removeElement(user.getPseudo());
	}

	private void FermerFenetre() {
		this.setVisible(false);
	}
}
