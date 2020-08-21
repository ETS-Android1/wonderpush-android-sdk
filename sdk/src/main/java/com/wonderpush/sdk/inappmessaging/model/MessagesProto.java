// Generated by the protocol buffer compiler.  DO NOT EDIT!

package com.wonderpush.sdk.inappmessaging.model;

import com.wonderpush.sdk.ActionModel;

import org.json.JSONObject;

import java.util.List;

public final class MessagesProto {
  private MessagesProto() {}

  /**
   * <pre>
   * Content of an in-app message.
   * </pre>
   *
   */
  public  static final class Content {

    private int messageDetailsCase_ = 0;
    private Object messageDetails_;
    private JSONObject dataBundle_ = new JSONObject();

    public Content() {
    }

    public JSONObject getDataBundle() {
      return dataBundle_;
    }

    public void setDataBundle(JSONObject dataBundle) {
      this.dataBundle_ = dataBundle == null ? new JSONObject() : dataBundle;
    }

    public enum MessageDetailsCase {
      BANNER(1),
      MODAL(2),
      IMAGE_ONLY(3),
      CARD(4),
      MESSAGEDETAILS_NOT_SET(0);
      private final int value;
      private MessageDetailsCase(int value) {
        this.value = value;
      }
      /**
       * @deprecated Use {@link #forNumber(int)} instead.
       */
      @Deprecated
      public static MessageDetailsCase valueOf(int value) {
        return forNumber(value);
      }

      public static MessageDetailsCase forNumber(int value) {
        switch (value) {
          case 1: return BANNER;
          case 2: return MODAL;
          case 3: return IMAGE_ONLY;
          case 4: return CARD;
          case 0: return MESSAGEDETAILS_NOT_SET;
          default: return null;
        }
      }
      public int getNumber() {
        return this.value;
      }
    };

    public MessageDetailsCase
    getMessageDetailsCase() {
      return MessageDetailsCase.forNumber(
          messageDetailsCase_);
    }

    private void clearMessageDetails() {
      messageDetailsCase_ = 0;
      messageDetails_ = null;
    }

    /**
     * <pre>
     * Banner mode
     * </pre>
     *
     * <code>optional .inappmessaging.BannerMessage banner = 1;</code>
     */
    public MessagesProto.BannerMessage getBanner() {
      if (messageDetailsCase_ == 1) {
         return (MessagesProto.BannerMessage) messageDetails_;
      }
      return null;
    }
    /**
     * <pre>
     * Banner mode
     * </pre>
     *
     * <code>optional .inappmessaging.BannerMessage banner = 1;</code>
     */
    public void setBanner(MessagesProto.BannerMessage value) {
      messageDetails_ = value;
      if (value != null) {
        messageDetailsCase_ = 1;
      } else {
        messageDetailsCase_ = 0;
      }
    }
    /**
     * <pre>
     * Banner mode
     * </pre>
     *
     * <code>optional .inappmessaging.BannerMessage banner = 1;</code>
     */
    private void clearBanner() {
      if (messageDetailsCase_ == 1) {
        messageDetailsCase_ = 0;
        messageDetails_ = null;
      }
    }

    /**
     * <pre>
     * Modal mode
     * </pre>
     *
     * <code>optional .inappmessaging.ModalMessage modal = 2;</code>
     */
    public MessagesProto.ModalMessage getModal() {
      if (messageDetailsCase_ == 2) {
         return (MessagesProto.ModalMessage) messageDetails_;
      }
      return null;
    }
    /**
     * <pre>
     * Modal mode
     * </pre>
     *
     * <code>optional .inappmessaging.ModalMessage modal = 2;</code>
     */
    public void setModal(MessagesProto.ModalMessage value) {
      messageDetails_ = value;
      if (value != null) {
        messageDetailsCase_ = 2;
      } else {
        messageDetailsCase_ = 0;
      }
    }
    /**
     * <pre>
     * Modal mode
     * </pre>
     *
     * <code>optional .inappmessaging.ModalMessage modal = 2;</code>
     */
    private void clearModal() {
      if (messageDetailsCase_ == 2) {
        messageDetailsCase_ = 0;
        messageDetails_ = null;
      }
    }

    /**
     * <pre>
     * Entire message is provided by the developer as an image
     * </pre>
     *
     * <code>optional .inappmessaging.ImageOnlyMessage image_only = 3;</code>
     */
    public MessagesProto.ImageOnlyMessage getImageOnly() {
      if (messageDetailsCase_ == 3) {
         return (MessagesProto.ImageOnlyMessage) messageDetails_;
      }
      return null;
    }
    /**
     * <pre>
     * Entire message is provided by the developer as an image
     * </pre>
     *
     * <code>optional .inappmessaging.ImageOnlyMessage image_only = 3;</code>
     */
    public void setImageOnly(MessagesProto.ImageOnlyMessage value) {
      messageDetails_ = value;
      if (value != null) {
        messageDetailsCase_ = 3;
      } else {
        messageDetailsCase_ = 0;
      }
    }
    /**
     * <pre>
     * Entire message is provided by the developer as an image
     * </pre>
     *
     * <code>optional .inappmessaging.ImageOnlyMessage image_only = 3;</code>
     */
    private void clearImageOnly() {
      if (messageDetailsCase_ == 3) {
        messageDetailsCase_ = 0;
        messageDetails_ = null;
      }
    }

    /**
     * <pre>
     * Card mode
     * </pre>
     *
     * <code>optional .inappmessaging.CardMessage card = 4;</code>
     */
    public MessagesProto.CardMessage getCard() {
      if (messageDetailsCase_ == 4) {
         return (MessagesProto.CardMessage) messageDetails_;
      }
      return null;
    }
    /**
     * <pre>
     * Card mode
     * </pre>
     *
     * <code>optional .inappmessaging.CardMessage card = 4;</code>
     */
    public void setCard(MessagesProto.CardMessage value) {
      messageDetails_ = value;
      if (value != null) {
        messageDetailsCase_ = 4;
      } else {
        messageDetailsCase_ = 0;
      }
    }
    /**
     * <pre>
     * Card mode
     * </pre>
     *
     * <code>optional .inappmessaging.CardMessage card = 4;</code>
     */
    private void clearCard() {
      if (messageDetailsCase_ == 4) {
        messageDetailsCase_ = 0;
        messageDetails_ = null;
      }
    }

  }

  /**
   * Protobuf type {@code .inappmessaging.CardMessage}
   */
  public  static final class CardMessage {
    private MessagesProto.Text title_;
    private MessagesProto.Text body_;
    private String portraitImageUrl_;
    private String landscapeImageUrl_;
    private String backgroundHexColor_;
    private MessagesProto.Button primaryActionButton_;
    private List<ActionModel> primaryActions_;
    private MessagesProto.Button secondaryActionButton_;
    private List<ActionModel> secondaryActions_;

    public CardMessage() {
    }

    /**
     * <pre>
     * [required] Message title.
     * </pre>
     *
     * <code>optional .inappmessaging.Text title = 1;</code>
     */
    public boolean hasTitle() {
      return title_ != null;
    }
    /**
     * <pre>
     * [required] Message title.
     * </pre>
     *
     * <code>optional .inappmessaging.Text title = 1;</code>
     */
    public MessagesProto.Text getTitle() {
      return title_;
    }
    /**
     * <pre>
     * [required] Message title.
     * </pre>
     *
     * <code>optional .inappmessaging.Text title = 1;</code>
     */
    public void setTitle(MessagesProto.Text value) {
      title_ = value;

      }

    /**
     * <pre>
     * [optional] Message body.
     * </pre>
     *
     * <code>optional .inappmessaging.Text body = 2;</code>
     */
    public boolean hasBody() {
      return body_ != null;
    }
    /**
     * <pre>
     * [optional] Message body.
     * </pre>
     *
     * <code>optional .inappmessaging.Text body = 2;</code>
     */
    public MessagesProto.Text getBody() {
      return body_;
    }
    /**
     * <pre>
     * [optional] Message body.
     * </pre>
     *
     * <code>optional .inappmessaging.Text body = 2;</code>
     */
    public void setBody(MessagesProto.Text value) {
      body_ = value;

      }

    /**
     * <pre>
     * [required] HTTPS url of the image to display in portrait orientation of
     * the device.
     * </pre>
     *
     * <code>optional string portrait_image_url = 3;</code>
     */
    public String getPortraitImageUrl() {
      return portraitImageUrl_;
    }
    /**
     * <pre>
     * [required] HTTPS url of the image to display in portrait orientation of
     * the device.
     * </pre>
     *
     * <code>optional string portrait_image_url = 3;</code>
     */
    public void setPortraitImageUrl(
        String value) {

      portraitImageUrl_ = value;
    }

    /**
     * <pre>
     * [optional] HTTPS url of the image to display in landscape orientation
     * of the device. If missing, the client will default to using the
     * portrait image url.
     * </pre>
     *
     * <code>optional string landscape_image_url = 4;</code>
     */
    public String getLandscapeImageUrl() {
      return landscapeImageUrl_;
    }
    /**
     * <pre>
     * [optional] HTTPS url of the image to display in landscape orientation
     * of the device. If missing, the client will default to using the
     * portrait image url.
     * </pre>
     *
     * <code>optional string landscape_image_url = 4;</code>
     */
    public void setLandscapeImageUrl(
        String value) {

      landscapeImageUrl_ = value;
    }

    /**
     * <pre>
     * [required] Message background color.
     * </pre>
     *
     * <code>optional string background_hex_color = 5;</code>
     */
    public String getBackgroundHexColor() {
      return backgroundHexColor_;
    }
    /**
     * <pre>
     * [required] Message background color.
     * </pre>
     *
     * <code>optional string background_hex_color = 5;</code>
     */
    public void setBackgroundHexColor(
        String value) {

      backgroundHexColor_ = value;
    }

    /**
     * <pre>
     * [required] Message button.
     * Clicking this button will trigger the modal's primary action
     * </pre>
     *
     * <code>optional .inappmessaging.Button primary_action_button = 6;</code>
     */
    public boolean hasPrimaryActionButton() {
      return primaryActionButton_ != null;
    }
    /**
     * <pre>
     * [required] Message button.
     * Clicking this button will trigger the modal's primary action
     * </pre>
     *
     * <code>optional .inappmessaging.Button primary_action_button = 6;</code>
     */
    public MessagesProto.Button getPrimaryActionButton() {
      return primaryActionButton_;
    }
    /**
     * <pre>
     * [required] Message button.
     * Clicking this button will trigger the modal's primary action
     * </pre>
     *
     * <code>optional .inappmessaging.Button primary_action_button = 6;</code>
     */
    public void setPrimaryActionButton(MessagesProto.Button value) {
      primaryActionButton_ = value;

      }

    /**
     * <pre>
     * [required] The action to take when clicking on the primary button.
     * This is required if primary action button is set
     * </pre>
     *
     * <code>optional .inappmessaging.Action primary_action = 7;</code>
     */
    public boolean hasPrimaryActions() {
      return primaryActions_ != null && primaryActions_.size() > 0;
    }
    /**
     * <pre>
     * [required] The action to take when clicking on the primary button.
     * This is required if primary action button is set
     * </pre>
     *
     * <code>optional .inappmessaging.Action primary_action = 7;</code>
     */
    public List<ActionModel> getPrimaryActions() {
      return primaryActions_;
    }
    /**
     * <pre>
     * [required] The action to take when clicking on the primary button.
     * This is required if primary action button is set
     * </pre>
     *
     * <code>optional .inappmessaging.Action primary_action = 7;</code>
     */
    public void setPrimaryActions(List<ActionModel> value) {
      primaryActions_ = value;
    }

    /**
     * <pre>
     * [optional] Message button.
     * Clicking this button will trigger the modal's secondary action
     * If the secondary action is not provided, the button simply dismisses the
     * message.
     * </pre>
     *
     * <code>optional .inappmessaging.Button secondary_action_button = 8;</code>
     */
    public boolean hasSecondaryActionButton() {
      return secondaryActionButton_ != null;
    }
    /**
     * <pre>
     * [optional] Message button.
     * Clicking this button will trigger the modal's secondary action
     * If the secondary action is not provided, the button simply dismisses the
     * message.
     * </pre>
     *
     * <code>optional .inappmessaging.Button secondary_action_button = 8;</code>
     */
    public MessagesProto.Button getSecondaryActionButton() {
      return secondaryActionButton_;
    }
    /**
     * <pre>
     * [optional] Message button.
     * Clicking this button will trigger the modal's secondary action
     * If the secondary action is not provided, the button simply dismisses the
     * message.
     * </pre>
     *
     * <code>optional .inappmessaging.Button secondary_action_button = 8;</code>
     */
    public void setSecondaryActionButton(MessagesProto.Button value) {
      secondaryActionButton_ = value;

      }

    /**
     * <pre>
     * [optional] The action to take when the secondary action button is clicked.
     * </pre>
     *
     * <code>optional .inappmessaging.Action secondary_action = 9;</code>
     */
    public boolean hasSecondaryActions() {
      return secondaryActions_ != null && secondaryActions_.size() > 0;
    }
    /**
     * <pre>
     * [optional] The action to take when the secondary action button is clicked.
     * </pre>
     *
     * <code>optional .inappmessaging.Action secondary_action = 9;</code>
     */
    public List<ActionModel> getSecondaryActions() {
      return secondaryActions_;
    }
    /**
     * <pre>
     * [optional] The action to take when the secondary action button is clicked.
     * </pre>
     *
     * <code>optional .inappmessaging.Action secondary_action = 9;</code>
     */
    public void setSecondaryActions(List<ActionModel> value) {
      secondaryActions_ = value;
    }

  }

  /**
   * Protobuf type {@code .inappmessaging.ImageOnlyMessage}
   */
  public  static final class ImageOnlyMessage {
    private String imageUrl_;
    private List<ActionModel> actions_;
    private InAppMessage.CloseButtonPosition closeButtonPosition_;

    public ImageOnlyMessage() {
    }

    /**
     * <pre>
     * The url of the image to be sent as the message.
     * </pre>
     *
     * <code>optional string image_url = 1;</code>
     */
    public String getImageUrl() {
      return imageUrl_;
    }
    /**
     * <pre>
     * The url of the image to be sent as the message.
     * </pre>
     *
     * <code>optional string image_url = 1;</code>
     */
    public void setImageUrl(
        String value) {
      imageUrl_ = value;
    }

    /**
     * <pre>
     * [optional] The Action to take when clicking anywhere on the image.
     * </pre>
     *
     * <code>optional .inappmessaging.Action action = 2;</code>
     */
    public boolean hasActions() {
      return actions_ != null && actions_.size() > 0;
    }
    /**
     * <pre>
     * [optional] The Action to take when clicking anywhere on the image.
     * </pre>
     *
     * <code>optional .inappmessaging.Action action = 2;</code>
     */
    public List<ActionModel> getActions() {
      return actions_;
    }
    /**
     * <pre>
     * [optional] The Action to take when clicking anywhere on the image.
     * </pre>
     *
     * <code>optional .inappmessaging.Action action = 2;</code>
     */
    public void setActions(List<ActionModel> value) {
      actions_ = value;
    }

    public void setCloseButtonPosition(InAppMessage.CloseButtonPosition closeButtonPosition) {
      closeButtonPosition_ = closeButtonPosition;
    }

    public InAppMessage.CloseButtonPosition getCloseButtonPosition() {
      return closeButtonPosition_;
    }

  }

  /**
   * Protobuf type {@code .inappmessaging.BannerMessage}
   */
  public  static final class BannerMessage {
    private MessagesProto.Text title_;
    private MessagesProto.Text body_;
    private String imageUrl_;
    private List<ActionModel> actions_;
    private String backgroundHexColor_;

    public BannerMessage() {
    }

    /**
     * <pre>
     * [required] Message title.
     * </pre>
     *
     * <code>optional .inappmessaging.Text title = 1;</code>
     */
    public boolean hasTitle() {
      return title_ != null;
    }
    /**
     * <pre>
     * [required] Message title.
     * </pre>
     *
     * <code>optional .inappmessaging.Text title = 1;</code>
     */
    public MessagesProto.Text getTitle() {
      return title_;
    }
    /**
     * <pre>
     * [required] Message title.
     * </pre>
     *
     * <code>optional .inappmessaging.Text title = 1;</code>
     */
    public void setTitle(MessagesProto.Text value) {
      title_ = value;

      }

    /**
     * <pre>
     * [optional] Message body.
     * </pre>
     *
     * <code>optional .inappmessaging.Text body = 2;</code>
     */
    public boolean hasBody() {
      return body_ != null;
    }
    /**
     * <pre>
     * [optional] Message body.
     * </pre>
     *
     * <code>optional .inappmessaging.Text body = 2;</code>
     */
    public MessagesProto.Text getBody() {
      return body_;
    }
    /**
     * <pre>
     * [optional] Message body.
     * </pre>
     *
     * <code>optional .inappmessaging.Text body = 2;</code>
     */
    public void setBody(MessagesProto.Text value) {
      body_ = value;

      }

    /**
     * <pre>
     * [optional] Image to display (https url - it will be downloaded onto the
     * device) No prefix required.
     * </pre>
     *
     * <code>optional string image_url = 3;</code>
     */
    public String getImageUrl() {
      return imageUrl_;
    }
    /**
     * <pre>
     * [optional] Image to display (https url - it will be downloaded onto the
     * device) No prefix required.
     * </pre>
     *
     * <code>optional string image_url = 3;</code>
     */
    public void setImageUrl(
        String value) {

      imageUrl_ = value;
    }

    /**
     * <pre>
     * [optional] Action to take when banner is clicked
     * </pre>
     *
     * <code>optional .inappmessaging.Action action = 4;</code>
     */
    public boolean hasActions() {
      return actions_ != null && actions_.size() > 0;
    }
    /**
     * <pre>
     * [optional] Action to take when banner is clicked
     * </pre>
     *
     * <code>optional .inappmessaging.Action action = 4;</code>
     */
    public List<ActionModel> getActions() {
      return actions_;
    }
    /**
     * <pre>
     * [optional] Action to take when banner is clicked
     * </pre>
     *
     * <code>optional .inappmessaging.Action action = 4;</code>
     */
    public void setActions(List<ActionModel> value) {
      actions_ = value;
    }

    /**
     * <pre>
     * [optional]  Message background color.
     * </pre>
     *
     * <code>optional string background_hex_color = 5;</code>
     */
    public String getBackgroundHexColor() {
      return backgroundHexColor_;
    }
    /**
     * <pre>
     * [optional]  Message background color.
     * </pre>
     *
     * <code>optional string background_hex_color = 5;</code>
     */
    public void setBackgroundHexColor(
        String value) {
      backgroundHexColor_ = value;
    }

  }

  /**
   * Protobuf type {@code .inappmessaging.ModalMessage}
   */
  public  static final class ModalMessage {
    private MessagesProto.Text title_;
    private MessagesProto.Text body_;
    private String imageUrl_;
    private MessagesProto.Button actionButton_;
    private List<ActionModel> actions_;
    private String backgroundHexColor_;
    private InAppMessage.CloseButtonPosition closeButtonPosition_;

    public ModalMessage() {
    }

    /**
     * <pre>
     * [required] Message title.
     * </pre>
     *
     * <code>optional .inappmessaging.Text title = 1;</code>
     */
    public boolean hasTitle() {
      return title_ != null;
    }
    /**
     * <pre>
     * [required] Message title.
     * </pre>
     *
     * <code>optional .inappmessaging.Text title = 1;</code>
     */
    public MessagesProto.Text getTitle() {
      return title_;
    }
    /**
     * <pre>
     * [required] Message title.
     * </pre>
     *
     * <code>optional .inappmessaging.Text title = 1;</code>
     */
    public void setTitle(MessagesProto.Text value) {
      title_ = value;

      }

    /**
     * <pre>
     * [optional] Message body.
     * </pre>
     *
     * <code>optional .inappmessaging.Text body = 2;</code>
     */
    public boolean hasBody() {
      return body_ != null;
    }
    /**
     * <pre>
     * [optional] Message body.
     * </pre>
     *
     * <code>optional .inappmessaging.Text body = 2;</code>
     */
    public MessagesProto.Text getBody() {
      return body_;
    }
    /**
     * <pre>
     * [optional] Message body.
     * </pre>
     *
     * <code>optional .inappmessaging.Text body = 2;</code>
     */
    public void setBody(MessagesProto.Text value) {
      body_ = value;

      }

    /**
     * <pre>
     * [optional] Image to display (https url - it will be downloaded onto the device)
     * No prefix required.
     * </pre>
     *
     * <code>optional string image_url = 3;</code>
     */
    public String getImageUrl() {
      return imageUrl_;
    }
    /**
     * <pre>
     * [optional] Image to display (https url - it will be downloaded onto the device)
     * No prefix required.
     * </pre>
     *
     * <code>optional string image_url = 3;</code>
     */
    public void setImageUrl(
        String value) {
      imageUrl_ = value;
    }

    /**
     * <pre>
     * [optional] Message button.
     * Clicking this button will trigger the modal's action
     * Currently, the two must be set together:
     * action.isSet() iff actionButton.isSet()
     * </pre>
     *
     * <code>optional .inappmessaging.Button action_button = 4;</code>
     */
    public boolean hasActionButton() {
      return actionButton_ != null;
    }
    /**
     * <pre>
     * [optional] Message button.
     * Clicking this button will trigger the modal's action
     * Currently, the two must be set together:
     * action.isSet() iff actionButton.isSet()
     * </pre>
     *
     * <code>optional .inappmessaging.Button action_button = 4;</code>
     */
    public MessagesProto.Button getActionButton() {
      return actionButton_;
    }
    /**
     * <pre>
     * [optional] Message button.
     * Clicking this button will trigger the modal's action
     * Currently, the two must be set together:
     * action.isSet() iff actionButton.isSet()
     * </pre>
     *
     * <code>optional .inappmessaging.Button action_button = 4;</code>
     */
    public void setActionButton(MessagesProto.Button value) {
      actionButton_ = value;

      }

    /**
     * <pre>
     * [optional] The Action to take when clicking on the button.
     * Currently, the two must be set together:
     * action.isSet() iff actionButton.isSet()
     * </pre>
     *
     * <code>optional .inappmessaging.Action action = 5;</code>
     */
    public boolean hasActions() {
      return actions_ != null && actions_.size() > 0;
    }
    /**
     * <pre>
     * [optional] The Action to take when clicking on the button.
     * Currently, the two must be set together:
     * action.isSet() iff actionButton.isSet()
     * </pre>
     *
     * <code>optional .inappmessaging.Action action = 5;</code>
     */
    public List<ActionModel> getActions() {
      return actions_;
    }
    /**
     * <pre>
     * [optional] The Action to take when clicking on the button.
     * Currently, the two must be set together:
     * action.isSet() iff actionButton.isSet()
     * </pre>
     *
     * <code>optional .inappmessaging.Action action = 5;</code>
     */
    public void setActions(List<ActionModel> value) {
      actions_ = value;
    }

    /**
     * <pre>
     * [optional]  Message background color.
     * </pre>
     *
     * <code>optional string background_hex_color = 6;</code>
     */
    public String getBackgroundHexColor() {
      return backgroundHexColor_;
    }
    /**
     * <pre>
     * [optional]  Message background color.
     * </pre>
     *
     * <code>optional string background_hex_color = 6;</code>
     */
    public void setBackgroundHexColor(
        String value) {
      backgroundHexColor_ = value;
    }

    public void setCloseButtonPosition(InAppMessage.CloseButtonPosition closeButtonPosition) {
      closeButtonPosition_ = closeButtonPosition;
    }

    public InAppMessage.CloseButtonPosition getCloseButtonPosition() {
      return closeButtonPosition_;
    }

  }

  /**
   * Protobuf type {@code .inappmessaging.Text}
   */
  public  static final class Text  {
    private String text_;
    private String hexColor_;

    public static Text fromJSON(JSONObject textJson) {
      if (textJson == null) return null;
      Text result = new Text();
      result.setText(textJson.optString("text"));
      result.setHexColor(textJson.optString("hexColor"));
      if (result.getText() == null) return null;
      return result;
    }

    private Text() {
    }

    /**
     * <pre>
     * Message text.
     * </pre>
     *
     * <code>optional string text = 1;</code>
     */
    public String getText() {
      return text_;
    }
    /**
     * <pre>
     * Message text.
     * </pre>
     *
     * <code>optional string text = 1;</code>
     */
    public void setText(
        String value) {
      text_ = value;
    }

    /**
     * <pre>
     * Message font color.
     * </pre>
     *
     * <code>optional string hex_color = 2;</code>
     */
    public String getHexColor() {
      return hexColor_;
    }
    /**
     * <pre>
     * Message font color.
     * </pre>
     *
     * <code>optional string hex_color = 2;</code>
     */
    public void setHexColor(
        String value) {

      hexColor_ = value;
    }

  }

  /**
   * Protobuf type {@code .inappmessaging.Button}
   */
  public  static final class Button {
    private MessagesProto.Text text_;
    private String buttonHexColor_;

    public static Button fromJSON(JSONObject buttonJson) {
      if (buttonJson == null) return null;
      Button result = new Button();
      Text text = Text.fromJSON(buttonJson.optJSONObject("text"));
      result.setText(text);
      result.setButtonHexColor(buttonJson.optString("buttonHexColor"));
      return result;
    }
    private Button() {
    }

    /**
     * <pre>
     * Button text.
     * </pre>
     *
     * <code>optional .inappmessaging.Text text = 1;</code>
     */
    public boolean hasText() {
      return text_ != null;
    }
    /**
     * <pre>
     * Button text.
     * </pre>
     *
     * <code>optional .inappmessaging.Text text = 1;</code>
     */
    public MessagesProto.Text getText() {
      return text_;
    }
    /**
     * <pre>
     * Button text.
     * </pre>
     *
     * <code>optional .inappmessaging.Text text = 1;</code>
     */
    public void setText(MessagesProto.Text value) {
      text_ = value;

      }

    /**
     * <pre>
     * Button background color.
     * </pre>
     *
     * <code>optional string button_hex_color = 2;</code>
     */
    public String getButtonHexColor() {
      return buttonHexColor_;
    }
    /**
     * <pre>
     * Button background color.
     * </pre>
     *
     * <code>optional string button_hex_color = 2;</code>
     */
    public void setButtonHexColor(
        String value) {

      buttonHexColor_ = value;
    }

  }

}
