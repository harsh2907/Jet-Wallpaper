
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
<p align="center">
  <img src="https://user-images.githubusercontent.com/71754826/209925781-8a8e2e11-4383-414c-9f63-7f98de5ed9cd.jpeg" width="200" style="margin-right: 80px;" alt="Image 1">
  <img src="https://user-images.githubusercontent.com/71754826/209925787-6e0371a4-fe5d-41d2-8382-af022b9396cf.jpeg" width="200" style="margin-right: 80px;" alt="Image 2">
  <img src="https://user-images.githubusercontent.com/71754826/209925790-ec1a7388-28c0-4ac2-8e99-e925cc0de9cb.jpeg" width="200" style="margin-right: 80px;" alt="Image 3">
  <img src="https://user-images.githubusercontent.com/71754826/209925794-434bb2ec-21dd-42d9-bc70-1bfcd90fd225.jpeg" width="200" alt="Image 4">
</p>

