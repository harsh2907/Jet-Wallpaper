
# Jet Wallpaper

The app is built using the latest Android technologies, including Jetpack Compose, Kotlin, Dagger Hilt, and MVVM architecture. It utilizes the Wallhaven API to provide a wide range of high-quality wallpapers for users to choose from and includes a favorites feature that allows users to save their favorite wallpapers to a local database for easy access. The app's user interface is easy to use and its focus on performance guarantees a smooth and efficient experience. Use the app to personalize your device and make it uniquely your own.
## API Reference

#### Search Wallpaper
#### Base Url: https://wallhaven.cc/

```http
  GET /api/v1/search
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `apiKey` | `string` | **Required**. Your API key |
| `q` | `string` | **Required**. Query |



## Deployment

To run this project. Find Constants in jetwallpaper/domain/utils
package and update it with your api key.

```bash
  object Constants{

    const val API_KEY:String = "YOUR API_KEY"

    //Other Variables
  }
```

## Screenshots
![Home Screen](/docs/screenshots/2.jpeg)
![Search Screen](/docs/screenshots/3.jpeg)
![Search Screen](/docs/screenshots/4.jpeg)
![Favourite Screen](/docs/screenshots/1.jpeg)
