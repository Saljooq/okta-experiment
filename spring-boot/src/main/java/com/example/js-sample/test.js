import fetch, {Headers} from 'node-fetch'


// COPY PASTE THE CODE BELOW - with updated access token after bearer if needed into your browser console to check if
// it'll work in a javascript SPA

var myHeaders = new Headers();
myHeaders.append("Authorization", "Bearer eyJraWQiOiJoVUNZZFVlS3c1MWN4dUpSMHVHd1NnSkhJQW9XalBhbnVuV0ZZZlgyMWswIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULndyX0FZbkZ3dmRXMmdZRFBKdHdHa1FnYm0yQWVXZXVMX1dVNkdpT1BtNnMiLCJpc3MiOiJodHRwczovL2Rldi05NTA3NTg2NS5va3RhLmNvbS9vYXV0aDIvZGVmYXVsdCIsImF1ZCI6ImFwaTovL2RlZmF1bHQiLCJpYXQiOjE2NjE2MjMyNDIsImV4cCI6MTY2MTYyNjg0MiwiY2lkIjoiMG9hNHg1ZXJ2eW0zWWlUNzI1ZDciLCJ1aWQiOiIwMHU0eDNzd2s1UXdBNmwzQjVkNyIsInNjcCI6WyJwcm9maWxlIiwib3BlbmlkIiwiZW1haWwiXSwiYXV0aF90aW1lIjoxNjYxNjIzMjQwLCJzdWIiOiJzYWx0YWZAaWFzdGF0ZS5lZHUifQ.Y8yIeAP2B2teAtcYhWXIwN29_kL0JpFTodrHk6A5R4t7EwfCbZDQJxuCGz3rGldsg4ziQlmxZ_Z8HBNv5n6vZFIJLIBgObH12TEg6bF_EBKKmg3EaiCKfiH9qTSIC0vZsOrq1SeuWQVr8i-Wfs5ZkGlFpT19vkXCFq5M3onqGEpcxrIkst7SLPxuOl414nTvpSgzemVqMJIWUhaYrM9at7a6ZfXEUSahUdiqheW3ei82hvlwDYgPQmmXuAX1wW5jamxCfyELQTTcHa2qC29pdefhNlPZ9ulH12ZCPIPqdwprrO9OzSYSrtbmItMkrg8X7OTXxneiKhq9E7agAFzKJg");

var requestOptions = {
  method: 'GET',
  headers: myHeaders,
  redirect: 'follow'
};

fetch("http://localhost:8080/resources", requestOptions)
  .then(response => {
    console.log("Response Status : ", response.status)
    return response.text()
  }
    )
  .then(result => console.log(result))
  .catch(error => console.log('error', error));