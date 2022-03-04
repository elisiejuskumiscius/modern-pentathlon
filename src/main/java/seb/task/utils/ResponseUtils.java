package seb.task.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seb.task.exceptions.ExecutableControllerCode;
import seb.task.exceptions.ResponseStatus;
import seb.task.exceptions.SebExceptionStatus;
import seb.task.exceptions.SebResponseException;
import seb.task.messages.ValidationMessage;

import java.util.List;
import java.util.function.Consumer;

public interface ResponseUtils {

    Logger logger = LogManager.getLogger(ResponseUtils.class);

    static void handleResponseMessageFormatting(
            Consumer<ResponseStatus> statusConsumer,
            Consumer<List<ValidationMessage>> validationMessageListConsumer,
            ExecutableControllerCode code) {

        try {
            code.run();
            statusConsumer.accept(ResponseStatus.OK);
        } catch (SebResponseException e) {
            if (!e.getValidationMessageList().isEmpty()) {
                String msg = "";
                for (ValidationMessage m : e.getValidationMessageList()) {
                    msg = msg + "\n\t" + m.getCode() + " - " + m.getText();
                }
                logger.error(String.format("ValidationMessages: %s", msg));

                validationMessageListConsumer.accept(e.getValidationMessageList());
            }

            if (SebExceptionStatus.NOT_IMPLEMENTED.equals(e.getStatus())) {
                statusConsumer.accept(ResponseStatus.NOTIMPLEMENTED);
            } else if (SebExceptionStatus.NODATA.equals(e.getStatus())) {
                statusConsumer.accept(ResponseStatus.NODATA);
            } else if (SebExceptionStatus.FAIL.equals(e.getStatus())) {
                statusConsumer.accept(ResponseStatus.FAIL);
            } else if (SebExceptionStatus.OK.equals(e.getStatus())) {
                statusConsumer.accept(ResponseStatus.OK);
            }
            logger.error(e.getMessage(), e);
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            statusConsumer.accept(ResponseStatus.FAIL);
        }
    }

}
