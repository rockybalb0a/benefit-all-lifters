# Benefit All Lifetrs
운동 기록들을 생성/조회/수정/삭제할 수 있는 앱. 기본 구현은 모두 끝난 상태. 

기본적으로 SBD, FS, OHP 이 다섯가지 종목을 다루며,  단관절 운동은 물론, 기타 다관절 운동(로우 등)들을 추가할 계획은 **전혀**없음.  

다만, 다섯 가지 종목을 좀 더 세분화할 계획임. 

- 예시 : Back squat 의 경우,
    - Back squat, High bar
    - Back squat, Low bar
    - Back squat, ATG
    - Back squat, Pause
    - Back squat, Slow eccentric 등으로 세분화.


## 시작하기

YouTube Api Key 발급
1. https://console.developers.google.com/apis 접속 후 좌측 라이브러리 선택

2. YouTube Data API v3 선택 후 사용 클릭

3. 사용자 인증 정보 > 사용자 인증 정보 만들기 > API 키 선택

4. 발급 받은 API키를 local.propertis 파일에 youtube_api_key="발급_받은_키" 로 저장 (아래 예시)

    ![image](https://user-images.githubusercontent.com/50101902/128626221-cf8228a6-e024-46a6-8e75-d6bbdbdf45ad.png)

5. 앱 실행

## 스크린샷

![image](https://user-images.githubusercontent.com/50101902/128626757-9f32f8d6-964b-4a6b-b5f6-0a17913b5704.png)
![image](https://user-images.githubusercontent.com/50101902/128626826-02c6e93c-ae32-4e70-8f7b-db56737c81bb.png)
![image](https://user-images.githubusercontent.com/50101902/128626759-1932a27a-bdef-402c-a7bc-7c2af1bea688.png)
![image](https://user-images.githubusercontent.com/50101902/128626760-a7aee93a-6644-4980-809d-e6ca2e9e09ab.png)
![image](https://user-images.githubusercontent.com/50101902/128626761-d9d98660-552a-4211-a1f3-7cb72cd420dd.png)
![image](https://user-images.githubusercontent.com/50101902/128626762-5b5f103b-78bb-48e9-8590-36a6d1a975f7.png)


## 스택
- Android Architecture Component

    - Data Binding
    
    - Navigation
    
    - LiveData
    
    - Room
    
    - ViewModel (MVVM)
    
- UI

    - Layout : Constraint layout, RecyclerView, ViewPager2, etc.
    
    - Animation : Motion layout
    
    
    
- Kotlin

    - Coroutines
    
    - Flow
    
- Third party libraries

    - Glide
    
    - Retrofit2 (with Moshi) & YouTube Data API V3
    
    - Hilt
