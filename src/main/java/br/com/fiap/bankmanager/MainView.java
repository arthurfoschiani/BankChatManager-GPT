package br.com.fiap.bankmanager;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
class MainView extends VerticalLayout {

    @Autowired
    private ChatService chatService;

    private List<MessageListItem> conversationHistory = new ArrayList<>();

    public MainView() {
        setupLayout();
    }

    private void setupLayout() {
        H1 header = new H1("Converse com o Gerente");
        MessageList chatMessages = new MessageList();
        HorizontalLayout inputArea = createInputArea(chatMessages);

        chatMessages.setHeightFull();
        chatMessages.setWidthFull();
        chatMessages.getStyle().set("background-color", "#f2f2f2");

        add(header, chatMessages, inputArea);
        setSizeFull();
    }

    private HorizontalLayout createInputArea(MessageList chatMessages) {
        TextField inputField = new TextField();
        inputField.setPlaceholder("Digite aqui suas dúvidas");
        inputField.setClearButtonVisible(true);
        inputField.setWidthFull();
        inputField.setPrefixComponent(new Icon(VaadinIcon.CHAT));

        Button sendButton = new Button("Enviar", new Icon(VaadinIcon.PAPERPLANE));
        sendButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        sendButton.addClickListener(event -> sendMessage(inputField, chatMessages));

        HorizontalLayout layout = new HorizontalLayout(inputField, sendButton);
        layout.setWidthFull();
        return layout;
    }

    private void sendMessage(TextField inputField, MessageList chatMessages) {
        var timestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        MessageListItem userMessage = new MessageListItem(inputField.getValue(), timestamp, "Cliente");
        conversationHistory.add(userMessage);

        String reply = chatService.sendMessage(inputField.getValue());
        MessageListItem responseMessage = new MessageListItem(reply, timestamp, "Gerente");
        conversationHistory.add(responseMessage);

        chatMessages.setItems(conversationHistory);
        inputField.clear();
    }
}