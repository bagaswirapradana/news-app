# News App (Berta - Berita Kita)
This application is connected to the internet and retrieves news from the News API using OkHttp, which is then parsed using Gson. The information described is then presented as news feed. Navigation Drawer displays news sources from the News API with the help of List View and Fragment, making it easier for users to access news sources with just a finger sweep.
## Objective
* A page which display list of news sources (ex: BBC, TheVerge, dll)
* If user click one of the news source, open a new page and display a list of articles from that source
* If user click one of the article, open a webview to display the article
* On list of articles, provide a search function to search article titles and display the result
## Tools
* Android Studio v3.1.4
* [News API](https://newsapi.org/)
* [Okhttp](http://square.github.io/okhttp/)
* [GSON](https://github.com/google/gson)
## API Key
You must provide your own News API key in order to connect with the API and fetch news data from it. Just put your API key into ``` ~/.config/Config.java ``` file (create the file if it does not exist already):
```
public static final String API_KEY = "Your own KEY News API";
```
## Screenshots
<img src="https://raw.githubusercontent.com/bagaswirapradana/news-app/master/screenshoot_news.png" data-canonical-src="https://raw.githubusercontent.com/bagaswirapradana/news-app/master/screenshoot_news.png" width="300"/>

## License
```
Copyright 2018 Bagas Wira Pradana

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
