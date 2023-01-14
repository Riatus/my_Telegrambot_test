import functions.FilterOperation;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import utils.ImageUtils;
import utils.PhotoMessageUtils;
import utils.RgbMaster;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    // Название бота
    @Override
    public String getBotUsername() {
        return "vladsecond_bot";
    }

    // токен бота
    @Override
    public String getBotToken() {

        return "-";
    }

    @Override
    public void onUpdateReceived(Update update) {
        // получение и сохранение изображение пользователя
        Message message = update.getMessage();
        String chatID = message.getChatId().toString();
        try {
            ArrayList<String> photoPaths = new ArrayList<>(PhotoMessageUtils.savePhotos(getFilesByMessage(message), getBotToken()));
                for (String path: photoPaths) {
                    PhotoMessageUtils.processingImage(path);
                    execute(preparePhotoMessage(path,chatID));
                }
        }catch (Exception e){
                e.printStackTrace();
            }

    }

    private List<org.telegram.telegrambots.meta.api.objects.File> getFilesByMessage(Message message) {
        List<PhotoSize> photoSizes = message.getPhoto();
        ArrayList<org.telegram.telegrambots.meta.api.objects.File> files = new ArrayList<>();
        for (PhotoSize photoSize : photoSizes
        ) {
            final String fileId = photoSize.getFileId();
            try {
               files.add(sendApiMethod(new GetFile(fileId)));

            } catch (TelegramApiException  e) {
                e.printStackTrace();
            }
        }
        return  files;
    }
    /// подготовка отправки изображение обратно
    private SendPhoto preparePhotoMessage(String localPath, String chatID){
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatID);
        InputFile newFile = new InputFile();
        newFile.setMedia(new File(localPath));
        sendPhoto.setPhoto(newFile);
        return sendPhoto;
    }

}
