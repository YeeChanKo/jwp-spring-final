# 자바 웹 프로그래밍 최종 점검
## 최종 점검 문서 
* JWP Basic 최종 점검.pdf 문서를 참고해 최종 점검 진행

## 질문에 대한 답변
#### 2. Tomcat 서버의 시작 과정을 설명하시오.
톰캣 서버가 시작하면 스프링 프레임웍이 WebApplicationInitializer를 구현한 MyWebInitializer를 실행시켜 초기화 작업을 진행한다. 부모 Application Context(컨테이너)에 부모 설정 파일 AppConfig.class를 등록해주고, 서블릿 컨테이너에 이 부모 컨테이너를 등록한다. 그다음 자식 Application Context(컨테이너)를 생성해 부모 컨테이너를 부모로 설정해주고 자식 컨테이너에 자식 설정 파일 WebMvcConfig.class를 등록해준다. 이 자식 컨테이너에서 새로운 DispatcherServlet을 생성하고 이 서블릿을 서블릿 컨테이너에 추가해준다. 이렇게 생성된 디스패처 서블릿을 구동시에 로드하게 하고 모든 클라이언트 요청 경로에 대해 맵핑되게 설정한다.


#### 3. http://localhost:8080 으로 요청했을 때의 과정을 설명하시오.


#### 6. QuestionController가 multi thread에서 문제가 되는 이유를 설명하시오.
