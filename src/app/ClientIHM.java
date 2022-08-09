package app;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class ClientIHM extends JFrame {

    private final ClientManagement clientManagement;

    private final JPanel container;
    private final GridBagConstraints gc;

    public ClientIHM(ClientManagement clientManagement, String title) throws HeadlessException {
        super(title);
        container = (JPanel) this.getContentPane();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800,600);
        setLocationRelativeTo(null);
        gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(5, 5, 5, 5);
        gc.fill = GridBagConstraints.NONE;
        gc.ipady = GridBagConstraints.CENTER;
        gc.ipadx = GridBagConstraints.CENTER;

        this.clientManagement = clientManagement;
    }

    public void start() {
        setClientNameView();
    }

    private void initContainer(int cols, int rows) {
        setVisible(false);
        container.removeAll();
        container.setLayout(new GridBagLayout());
        gc.gridwidth = cols;
        gc.gridheight = rows;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.insets = new Insets(5, 5, 5, 5);
    }

    private void setComponentSize(JComponent component, int width, int height) {
        component.setPreferredSize(new Dimension(width, height));
    }

    private void setPosition(int col, int row, int length, int height) {
        gc.gridx = col;
        gc.gridy = row;
        gc.gridwidth = length;
        gc.gridheight = height;
    }

    private void setPosition(int col, int row) {
        gc.gridx = col;
        gc.gridy = row;
        gc.gridwidth = 1;
        gc.gridheight = 1;
    }

    private void setClientNameView() {
        initContainer(1,1);

        JLabel label = new JLabel("Veuillez saisir votre pseudo pour la session");
        setPosition(0,0,2,1);
        gc.insets = new Insets(5, 5, 15, 5);
        gc.anchor = GridBagConstraints.PAGE_END;
        container.add(label, gc);

        JTextField clientNameField = new JTextField();
        setPosition(0,1);
        setComponentSize(clientNameField, 200,30);
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        container.add(clientNameField, gc);

        JButton send = new JButton("Envoyer");
        setPosition(1,1);
        setComponentSize(send, 80,30);
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        container.add(send, gc);

        send.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setClientName(clientNameField.getText());
            }
        });

        setVisible(true);
    }

    private void chooseActionView() {
        initContainer(3,2);

        JLabel label = new JLabel("Bienvenue " + clientManagement.getClientName() + " - Choisissez une action" );
        setPosition(1,1,3,1);
        gc.insets = new Insets(5, 5, 50, 5);
        gc.anchor = GridBagConstraints.PAGE_END;
        container.add(label, gc);

        gc.anchor = GridBagConstraints.PAGE_START;

        JButton create = new JButton("Create");
        setPosition(1,2);
        setComponentSize(create, 150,50);
        container.add(create, gc);

        create.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                chooseAction("create");
            }
        });

        JButton join = new JButton("Join");
        setPosition(2,2);
        setComponentSize(join, 150,50);
        container.add(join, gc);

        join.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                chooseAction("join");
            }
        });

        JButton exit = new JButton("Exit");
        setPosition(3,2);
        setComponentSize(exit, 150,50);
        container.add(exit, gc);

        exit.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                chooseAction("exit");
                dispose();
            }
        });

        setVisible(true);
    }

    private void createChannelView() {
        initContainer(1,1);

        JLabel label = new JLabel("Veuillez saisir votre le nom du channel");
        setPosition(0,0,2,1);
        gc.insets = new Insets(5, 5, 15, 5);
        gc.anchor = GridBagConstraints.PAGE_END;
        container.add(label, gc);

        JTextField channelNameField = new JTextField();
        setPosition(0,1);
        setComponentSize(channelNameField, 200,30);
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        container.add(channelNameField, gc);

        JButton send = new JButton("Envoyer");
        setPosition(1,1);
        setComponentSize(send, 80,30);
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        container.add(send, gc);

        send.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                createChannel(channelNameField.getText());
            }
        });

        setVisible(true);
    }

    private void joinChannelView(String[] channels) {
        initContainer(1,1);

        JLabel label = new JLabel("Veuillez sélectionner un channel");
        setPosition(0,0,3,1);
        gc.insets = new Insets(5, 5, 15, 5);
        gc.anchor = GridBagConstraints.PAGE_END;
        container.add(label, gc);

        JComboBox<String> channelNameField = new JComboBox<>(channels);
        setPosition(0,1);
        setComponentSize(channelNameField, 450,30);
        gc.anchor = GridBagConstraints.PAGE_START;
        container.add(channelNameField, gc);

        JButton join = new JButton("Rejoindre");
        setPosition(1,1);
        setComponentSize(join, 120,30);
        gc.anchor = GridBagConstraints.PAGE_START;
        container.add(join, gc);

        JButton back = new JButton("Retour");
        setPosition(2,1);
        setComponentSize(back, 120,30);
        gc.anchor = GridBagConstraints.PAGE_START;
        container.add(back, gc);

        if (!channels[0].equals("")) {
            join.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    joinChannel((String) channelNameField.getSelectedItem());
                }
            });
        }

        back.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                chooseAction("back");
            }
        });

        setVisible(true);
    }

    private void chatInChannelView() {
        initContainer(3,3);

        JLabel label = new JLabel(clientManagement.getCurrentChannelName());
        setPosition(0,0,3,1);
        gc.insets = new Insets(5, 5, 15, 5);
        gc.anchor = GridBagConstraints.PAGE_END;
        container.add(label, gc);

        JEditorPane messages = new JEditorPane();
        messages.setEditable(false);

        HTMLEditorKit kit = new HTMLEditorKit();
        Document doc = kit.createDefaultDocument();
        StyleSheet styleSheet = kit.getStyleSheet();
        messages.setEditorKit(kit);
        messages.setDocument(doc);

        styleSheet.addRule(".right {text-align:right; padding: 3px; background-color:#ebf5fb;}");
        styleSheet.addRule(".left {text-align:left; padding: 3px; background-color:#fef9e7;}");

        JScrollPane chat = new JScrollPane(messages);
        setPosition(0,1,3,1);
        setComponentSize(chat, 760, 420);
        chat.setAutoscrolls(true);
        gc.anchor = GridBagConstraints.CENTER;
        container.add(chat, gc);

        JTextArea messageToSend = new JTextArea();
        messageToSend.setWrapStyleWord(true);
        messageToSend.setLineWrap(true);
        JScrollPane messageField = new JScrollPane(messageToSend);
        messageField.createVerticalScrollBar();
        setPosition(0,2);
        setComponentSize(messageField, 560,40);
        gc.anchor = GridBagConstraints.PAGE_END;
        container.add(messageField, gc);

        JButton send = new JButton("Envoyer");
        setPosition(1,2);
        setComponentSize(send, 80,40);
        gc.anchor = GridBagConstraints.PAGE_END;
        container.add(send, gc);

        send.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String message = messageToSend.getText();
                if (message.equals("exit")) {
                    leaveChannel();
                } else {
                    sendMessage(message);
                    messageToSend.setText("");
                    appendHTML(kit, doc, message);
                }
            }
        });

        JButton exit = new JButton("Quitter");
        setPosition(2,2);
        setComponentSize(exit, 80,40);
        gc.anchor = GridBagConstraints.PAGE_END;
        container.add(exit, gc);

        exit.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                leaveChannel();
            }
        });

        setVisible(true);
        clientManagement.runReader(kit, doc);
    }

    private void appendHTML(HTMLEditorKit kit, Document doc, String message) {
        String html = "<div class=\"right\"><span>" + message + "</span></div>";
        HTMLDocument htmlDocument = (HTMLDocument) doc;
        try {
            kit.insertHTML(htmlDocument, doc.getLength(), html, 0, 0, null);
        } catch (BadLocationException | IOException ignored) { }
    }

    /**
     *  Services
     */

    private void setClientName(String clientName) {
        clientManagement.setClientName(clientName);
        chooseActionView();
    }

    private void chooseAction(String action) {
        clientManagement.chooseAction(action);
        if (action.equals("create")) {
            createChannelView();
        }
        if (action.equals("join")) {
            String[] channels = clientManagement.getChannels();
            joinChannelView(channels);
        }
        if (action.equals("back")) {
            chooseActionView();
        }
    }

    private void createChannel(String channelName) {
        clientManagement.createChannel(channelName);
        chooseActionView();
    }

    private void joinChannel(String channelName) {
        clientManagement.joinChannel(channelName);
        chatInChannelView();
    }

    private void leaveChannel() {
        clientManagement.sendMessageInChannel("exit");
        chooseActionView();
    }

    private void sendMessage(String message) {
        clientManagement.sendMessageInChannel(message);
    }

}
