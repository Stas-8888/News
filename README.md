# NewsApp
- fetches news from an API https://newsapi.org/ and show them in a RecyclerView.
- support registration on Firebase. If not registered, some function won't work.
- supports searching news.
- supports choose news from 4 countries.
- supports choose categories of news.
- supports save,delete Article.
- supports day&night mode.
- supports check internet connections by BroadcastReceiver

# Preview
![5](https://user-images.githubusercontent.com/108537240/187301558-16fbb29f-9603-466e-825d-c6ae428f28f4.jpg)

# Libraries And Technologies used
- Clean Architecture: Data, Domain, Presentation.
- Firebase Authentication 
- Use Case: For business logic of project.
- Mapper: For separate entities.
- MVVM + StateFlow : Android architecture used to separate logic code from ui and save the
  application state in case the configuration changes.
- Retrofit + Gson Converter: Fetch news from rest api as a gson file and convert it to a kotlin
  object.
- Room : Save,delete and get the articles into a local database.
- RecyclerView with DiffUtil.
- Coroutines : Executing some code in the background.
- Dagger hilt : Dependency injection.
- Navigation Component : Navigate between fragments.
- Glide : Catch and cache images from the internet and show them in an imageView.
- viewBinding : to access the views without needing to inflate them.
