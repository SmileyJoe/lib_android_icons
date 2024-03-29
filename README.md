# Material Design Icons #

Package to load in icons from [Pictogrammers](https://pictogrammers.com/) in android

## Features ##

- Load any icon by it's name
- Apply a tint to icons
- Set an animated vector as a loading placeholder
- Set an icon to show if the icon isn't found
- Saves icons to a local Room DB
- Caches loaded icons into an LruCache
- IconImageView with custom attributes
- IconTextView with custom attributes to set an icon as the left drawable
- Helper class for using custom attributes in custom views
- Preload icons into the db

## Implementation ##

- Add the repo to the project `build.gradle`
  - `{github_user}`, your github username
  - `{github_token}`, a personal access token that has read packages permissions
  - More info on auth here](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry#authenticating-to-github-packages)
```groovy
allprojects {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/SmileyJoe/lib_android_icons")
            credentials {
                username = {github_user}
                password = {github_token}
            }
        }
    }
}
```
- Add the dependendy to app module `build.gradle`
```groovy
implementation 'io.smileyjoe:icons:{latest_version}'
```

## Setup ##

- Add the setup to the application classes `onCreate()`
```java
// preload names and listener are optional
Icon.setup(getApplicationContext(), preloadNames, new SetupListener());
```

## Useage ##
- Set an icon onto an `ImageView` and tint it `Color.BLUE`
```java
Icon.load(getBaseContext(), "freebsd", (icon) -> imageView.setImageDrawable(Icon.tint(icon, Color.BLUE)));
```

- Preload icons into the db
```java
Icon.load(getBaseContext(), "fridge", "format-size", "golf", "ghost");
```

## Views ##

There are two provided views with the package, and a helper class to handle alot of the work to make your own views.

The `IconImageView` and `IconTextView` both have the same attributes and work the same

### Custom attributes ###

- `icon_name`, name of the icon to load
- `icon_color`, color resource id
- `icon_placeholder`, drawable resource id for an animated vector, this isn't tinted by the `icon_color`
- `icon_missing`, drawable resource id for an image to show if the icon isn't found

### IconImageView ###

This is a view that extends `AppCompatImageView` and add's a few extra features.

- Custom attributes in xml
```xml
<io.smileyjoe.icons.view.IconImageView
    android:layout_width="50dp"
    android:layout_height="50dp"
    app:icon_name="emoticon-dead-outline"
    app:icon_color="@color/colorPrimary"
    app:icon_missing="@drawable/ic_broken"
    app:icon_placeholder="@drawable/anim_loading"/>
```

- Setting in icon and tint programatically
```java
// Set the color as a hex value
imageView.setTint("#00FF00");
// Set the color as a resource
imageView.setTint(R.color.colorPrimary);
imageView.setIcon("format-text-rotation-up");
```

### IconTextView ###

This is a view that extends `AppCompatTextView` and allows you to set the left drawable as an icon.

Useage is exactly the same as `IconImageView`

### Making your own ###

`IconViewHelper` will parse the custom attributes and give callbacks when icons are loaded, for a full example see `IconImageView`.

#### Basic usage ####

Use it inside your custom view

```java
private void init(AttributeSet attrs, int defStyle) {
    mHelper = new IconViewHelper(this);
    mHelper.setListener(new IconViewListener(){
        @Override
        public void onIconLoaded(Drawable icon) {
            setImageDrawable(Icon.tint(icon, mHelper.getColor(), true));
        }

        @Override
        public void showPlaceholder(AnimatedVectorDrawableCompat placeholder) {
            setImageDrawable(placeholder);
        }

        @Override
        public void showMissing(Drawable drawable) {
            setImageDrawable(Icon.tint(drawable, mHelper.getColor(), true));
        }
    });
    mHelper.load(attrs, defStyle);
}
```