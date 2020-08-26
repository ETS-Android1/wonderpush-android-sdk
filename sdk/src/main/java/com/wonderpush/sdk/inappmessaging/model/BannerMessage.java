// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.wonderpush.sdk.inappmessaging.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.wonderpush.sdk.ActionModel;
import com.wonderpush.sdk.NotificationMetadata;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/** Encapsulates an In App Banner Message. */
public class BannerMessage extends InAppMessage implements InAppMessage.InAppMessageWithImage {
  /*
   * !!!!!WARNING!!!!! We are overriding equality in this class. Please add equality checks for all
   * new private class members.
   */
  @NonNull private final Text title;

  @Nullable private final Text body;
  @Nullable private final ImageData imageData;
  @NonNull private final List<ActionModel> actions;
  @NonNull private final String backgroundHexColor;
  @NonNull private final BannerPosition bannerPosition;

  /** @hide */
  @Override
  public int hashCode() {
    int bodyHash = body != null ? body.hashCode() : 0;
    int imageHash = imageData != null ? imageData.hashCode() : 0;
    int actionHash = actions != null ? actions.hashCode() : 0;
    return title.hashCode() + bodyHash + imageHash + actionHash + backgroundHexColor.hashCode() + bannerPosition.hashCode();
  }

  /** @hide */
  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true; // same instance
    }
    if (!(o instanceof BannerMessage)) {
      return false; // not the correct instance type
    }
    BannerMessage b = (BannerMessage) o;
    if (hashCode() != b.hashCode()) {
      return false; // the hashcodes don't match
    }
    if (bannerPosition != b.bannerPosition) return false;

    if ((body == null && b.body != null) || (body != null && !body.equals(b.body))) {
      return false; // the bodies don't match
    }
    if ((imageData == null && b.imageData != null)
        || (imageData != null && !imageData.equals(b.imageData))) {
      return false; // the images don't match
    }
    if ((actions == null && b.actions != null) || (actions != null && !actions.equals(b.actions))) {
      return false; // the actions don't match
    }
    if (!title.equals(b.title)) {
      return false; // the tiles don't match
    }
    if (backgroundHexColor.equals(b.backgroundHexColor)) {
      return true; // everything matches
    }
    return false;
  }
  /*
   * !!!!!WARNING!!!!! We are overriding equality in this class. Please add equality checks for all
   * new private class members.
   */
  private BannerMessage(
      @NonNull NotificationMetadata notificationMetadata,
      @NonNull Text title,
      @Nullable Text body,
      @Nullable ImageData imageData,
      List<ActionModel> actions,
      @NonNull String backgroundHexColor,
      @Nullable BannerPosition bannerPosition,
      @NonNull JSONObject data) {
    super(notificationMetadata, MessageType.BANNER, data);
    this.title = title;
    this.body = body;
    this.imageData = imageData;
    this.actions = actions;
    this.backgroundHexColor = backgroundHexColor;
    this.bannerPosition = bannerPosition == null ? BannerPosition.TOP : bannerPosition;
  }

  /** Gets the title {@link Text} associated with this message */
  @NonNull
  public Text getTitle() {
    return title;
  }

  /** Gets the body {@link Text} associated with this message */
  @Nullable
  public Text getBody() {
    return body;
  }

  /** Gets the {@link ImageData} associated with this message */
  @Nullable
  public ImageData getImageData() {
    return imageData;
  }

  /** Gets the {@link ActionModel}s associated with this message */
  public List<ActionModel> getActions() {
    return actions;
  }

  /** Gets the {@link com.wonderpush.sdk.inappmessaging.model.InAppMessage.BannerPosition} associated with this message */
  public BannerPosition getBannerPosition() {
    return bannerPosition;
  }
  /** Gets the background hex color associated with this message */
  @NonNull
  public String getBackgroundHexColor() {
    return backgroundHexColor;
  }

  /**
   * only used by headless sdk and tests
   *
   * @hide
   */
  public static Builder builder() {
    return new BannerMessage.Builder();
  }

  @Override
  public ButtonType getButtonType(List<ActionModel> actions) {
    return actionsEqual(actions, this.actions) ? ButtonType.PRIMARY : ButtonType.UNDEFINED;
  }

  /**
   * Builder for {@link BannerMessage}
   *
   * @hide
   */
  public static class Builder {
    @Nullable
    Text title;
    @Nullable
    Text body;
    @Nullable ImageData imageData;
    @Nullable
    List<ActionModel> actions;
    @Nullable String backgroundHexColor;
    @Nullable private BannerPosition bannerPosition;

    public Builder setTitle(@Nullable Text title) {
      this.title = title;
      return this;
    }

    public Builder setBody(@Nullable Text body) {
      this.body = body;
      return this;
    }

    public Builder setImageData(@Nullable ImageData imageData) {
      this.imageData = imageData;
      return this;
    }

    public Builder setActions(@Nullable List<ActionModel> actions) {
      this.actions = actions;
      return this;
    }

    public Builder setBackgroundHexColor(@Nullable String backgroundHexColor) {
      this.backgroundHexColor = backgroundHexColor;
      return this;
    }

    public BannerMessage build(
            NotificationMetadata notificationMetadata, @NonNull JSONObject data) {
      if (title == null) {
        throw new IllegalArgumentException("Banner model must have a title");
      }
      if (TextUtils.isEmpty(backgroundHexColor)) {
        throw new IllegalArgumentException("Banner model must have a background color");
      }
      // We know backgroundColor is not null here because isEmpty checks for null.
      return new BannerMessage(
              notificationMetadata, title, body, imageData, actions == null ? new ArrayList<>() : actions, backgroundHexColor, bannerPosition, data);
    }

    public Builder setBannerPosition(@Nullable BannerPosition bannerPosition) {
      this.bannerPosition = bannerPosition;
      return this;
    }
  }
}
