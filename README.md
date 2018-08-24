

## 基于Java Web的学生信息管理系统

关键词：信息管理；java；JSP； MySQL；

该系统采用java语言结合JSP技术编写，以Eclipse作为开发工具，MySQL数据库作为后台数据库进行信息的存储，以及采用Tomcat作为对应的web服务器，运行JSP 页面和Servlet。

</br>

##  数据库设计

##### t_user表

| 字段名称  | 数据类型    |  长度  |    说明    |
| -------   | -----:      | :----: | :----:     |
| id        |   int       |   11   | 管理员ID   |
| username  | varchar     |   20   | 管理员姓名 |
| password  | varchar     |   20   | 管理员密码 |


##### t_grade表

| 字段名称  | 数据类型    |  长度  |    说明    |
| -------   | -----:      | :----: | :----:     |
| id        |   int       |   11   | 班级编号   |
| gradeName | varchar     |   20   | 班级名称   |
| gradeDesc | varchar     |  1000  | 班级详情   |

##### t_student表

| 字段名称  | 数据类型    |  长度  |    说明    |
| -------   | -----:      | :----: | :----:     |
| stuId     | int         |   11   | 学生ID     |
| stuNo     | varchar     |   20   | 学号       |
| stuName   | varchar     |   10   | 学生姓名   |
| sex       | varchar     |   5    | 性别       |
| birthday  | date        |   /    | 出生日期   |
| gradeId   | int         |   /    | 班级ID     |
| email     | varchar     |   20   | 邮箱       |
| stuDesc   | varchar     |  1000  | 学生详情   |

</br>

## 代码设计

本系统将后台java代码模块划分为util层（功能包层）、model层（对象封装层）、dao层（数据访问、操作数据层）、web层（即service层，业务处理并调用dao层）。系统划分具体如图所示:

![图片1.png](https://i.loli.net/2018/08/24/5b7f9c241f73e.png)

前台界面则分为登录界面，系统主界面，学生信息管理界面以及班级信息管理界面。如图

![图片2.png](https://i.loli.net/2018/08/24/5b7f9ca9447cf.png)
</br></br>

#### 登录界面

![图片3.png](https://i.loli.net/2018/08/24/5b7f9d6037e03.png)

###### 登录功能代码设计

登录界面index.jsp运用form表单，text信息框，password密码框，提交按钮以及重置按钮构建而成，引用外部css文件对页面进行进一步友好美化。其中通过调用javascript函数赋予重置按钮实现重置。javascript代码如下：

```
<script type="text/javascript">
	function resetValue() {
		document.getElementById("userName").value="";
		document.getElementById("password").value="";
	}
</script>
```


登录界面的功能是让管理员输入正确的身份验证以获得对系统的访问权限，管理员通过java的JDBC（Java DataBase Connectivity,java数据库连接），有了JDBC，向各种关系数据发送SQL语句就是一件很容易的事。本系统的JDBC核心代码如下：

```
public class DbUtil {
	private String dbUrl="jdbc:mysql://localhost:3306/db_studentInfo";
	private String dbUserName="root";
	private String dbPassword="123456";
	private String jdbcName="com.mysql.jdbc.Driver";

	public Connection getCon() throws Exception{  //获取数据库连接
		Class.forName(jdbcName);
		Connection con=DriverManager.getConnection(dbUrl,dbUserName,dbPassword);
		return con;
	}

	public void closeCon(Connection con) throws Exception{ //关闭数据库连接
		if(con!=null){
			con.close();
		}
	}
	public static void main(String[] args) {
		DbUtil dbUtil=new DbUtil();
		try {
			dbUtil.getCon();
			System.out.println("数据库连接成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
```
管理员登录系统时，在输入框输入管理员账号和密码，点击确定按钮，页面通过post递交数据，系统将会通过数据库中存储的数据进行数据比较。

在此之前需要对管理员对象进行封装成User.java， 再在java代码中dao层实现对后台数据库表t_user的访问，通过jdbc连接，用户可以在java中实现sql语句的操作。

下面的代码UserDao.java即实现用户对数据库数据的访问以此实现在表t_user中获得用户名及其密码，代码中的数字1，2代表SQL语句中问号对应的位置。其代码如下：

```
public class UserDao {
	/**
	 * 登录验证
	 * @param con
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public User login(Connection con,User user) throws Exception{
		User resultUser=null;
		String sql="select * from t_user where userName=? and password=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, user.getUserName());
		pstmt.setString(2, user.getPassword());
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()){
			resultUser=new User();
			resultUser.setUserName(rs.getString("userName"));
			resultUser.setPassword(rs.getString("password"));
		}
		return resultUser;
	}
}
```

帐号，密码填写完毕后，点击确认。如果用户的账号和姓名正确，进入学生信息管理系统主界面main.jsp ，否则提示帐号或密码错误。

建立登录验证功能需要通过servelt调用UserDao.java操作数据库得以实现，通过在util层封装的StringUtil中的isEmpty来判断所输入的帐号密码是否为空，接着通过UserDao中的login方法判断该用户是否为授权用户。下面是LoginServlet.java实现登录验证的核心代码：

```
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userName=request.getParameter("userName");
		String password=request.getParameter("password");
		request.setAttribute("userName", userName);
		request.setAttribute("password", password);
		if(StringUtil.isEmpty(userName)||StringUtil.isEmpty(password)){
			request.setAttribute("error", "用户名或密码为空！");
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}
		User user=new User(userName,password);
		Connection con=null;
		try {
			con=dbUtil.getCon();
			User currentUser=userDao.login(con, user);
			if(currentUser==null){
				request.setAttribute("error", "用户名或密码错误！");
				//服务器跳转
				request.getRequestDispatcher("index.jsp").forward(request, response);
			}else{
				//获取Session
				HttpSession session=request.getSession();
				session.setAttribute("currentUser", currentUser);
				//客户端跳转
				response.sendRedirect("main.jsp");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
```

#### 主界面

![图片4.png](https://i.loli.net/2018/08/24/5b7f9da2335a8.png)

###### 主界面代码设计

为了防止非授权用户越过登录界面index.jsp通过IP直接跳转到主界面main.jsp，将插入如下jsp代码到页面当中，当用户在没有输入帐号密码进行验证登录的情况下，系统强制跳转回登录界面。代码如下：

```
<%
	// 用户权限验证
	if(session.getAttribute("currentUser")==null){
		response.sendRedirect("index.jsp");
		return;
	}
%>
```

外链引入easyUI后，插入javascript代码进行左侧导航栏的构建，以其规范的书写格式对代码进行编写，openTab函数能通过赋值后的url把学生信息管理和班级信息管理内容展示到iframe框架内，管理元通过点击导航栏中的信息，实现管理员对班级信息和学生信息的操作。其代码如下：

```
<script type="text/javascript">
	$(function(){
		// 数据
		var treeData=[{
			text:"管理信息",
			children:[{
				text:"班级信息管理",
				attributes:{
					url:"gradeInfoManage.jsp"
				}
			},{
				text:"学生信息管理",
				attributes:{
					url:"studentInfoManage.jsp"
				}
			}]
		}];

		// 实例化树菜单
		$("#tree").tree({
			data:treeData,
			lines:true,
			onClick:function(node){
				if(node.attributes){
					openTab(node.text,node.attributes.url);
				}
			}
		});

		// 新增Tab
		function openTab(text,url){
			if($("#tabs").tabs('exists',text)){
				$("#tabs").tabs('select',text);
			}else{
				var content="<iframe frameborder='0' scrolling='auto' style='width:100%;height:100%' src="+url+"></iframe>";
				$("#tabs").tabs('add',{
					title:text,
					closable:true,
					content:content
				});
			}
		}
	});
</script>
```

#### 班级信息管理界面

![图片5.png](https://i.loli.net/2018/08/24/5b7f9dde769a0.png)

#### 学生信息管理界面

![图片6.png](https://i.loli.net/2018/08/24/5b7f9e3c6c6f8.png)

#### 学生、班级信息界面的功能（增删查改）详情请看代码。
