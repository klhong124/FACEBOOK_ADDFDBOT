import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JCheckBox;

public class AddFriendBot{
	private static JTextField tfusername;
	private static JPasswordField tfpassword;
	private static JTextField tftarget;
	private static WebDriver driver;
	private static JTextField tfspeed;
	private static JTextField tfidrange;
	private static JTextField tfnumber;
	private static JCheckBox chckbxRememberMe;
	private static ArrayList<String> idlist = new ArrayList<String>();
	private static HashMap<String,String> userlist = new HashMap<String,String>();


	@SuppressWarnings("unchecked")
	public static boolean checkFacebookID(String id) throws IOException, ClassNotFoundException {
		String location = "bin/"+tfusername.getText()+"-facebookIDList.txt";
		boolean result = true;
		File file =new File(location);
		if(file.exists()){
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(location));
			idlist = (ArrayList<String>) input.readObject();
			input.close();
			if (!idlist.contains(id)) {
				idlist.add(id);
				result = true;
			}else {
				result = false;
			}
		}else {
			idlist.add(id);
			result = true;
		}
		ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(location));
		output.writeObject(idlist);
		output.close();
		return result;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public static void UserInformationSave() throws ClassNotFoundException, IOException {
		String location = "bin/User Information.txt";
		File file =new File(location);
		if(file.exists()){
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(location));
			userlist = (HashMap<String,String>) input.readObject();
			input.close();
			boolean isSelected = chckbxRememberMe.isSelected();
			if(isSelected) {
				userlist.put("username",tfusername.getText());
				userlist.put("password",tfpassword.getText());
				userlist.put("target",tftarget.getText());
				userlist.put("number",tfnumber.getText());
				userlist.put("checkbox","true");
				}else {
					userlist.put("username","");
					userlist.put("password","");
					userlist.put("target","");
					userlist.put("number","999");
					userlist.put("checkbox","");
				}
			}else {
				userlist.put("username","");
				userlist.put("password","");
				userlist.put("target","");
				userlist.put("number","999");
				userlist.put("checkbox","true");
			}
		ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(location));
		output.writeObject(userlist);
		output.close();
	}

	@SuppressWarnings({ "unchecked" })
	public static HashMap<String,String> UserInformationRead() throws ClassNotFoundException, IOException {
		new File("bin").mkdirs();
		String location = "bin/User Information.txt";
		File file =new File(location);
		if(file.exists()){
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(location));
			userlist = (HashMap<String,String>) input.readObject();
			input.close();
			}else {
				userlist.put("username","");
				userlist.put("password","");
				userlist.put("target","");
				userlist.put("number","999");
				userlist.put("checkbox","true");
				ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(location));
				output.writeObject(userlist);
				output.close();
			}
		return userlist;
	}

	public static void LaunchGUIPanel() throws ClassNotFoundException, IOException {
		userlist = UserInformationRead();
		JFrame frame = new JFrame("Facebook Add Friend Bot");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(522, 351);
        frame.getContentPane().setLayout(null);

        JLabel lblUsername = new JLabel("Username :");
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblUsername.setBounds(21, 21, 182, 26);
        frame.getContentPane().add(lblUsername);

        JLabel lblPassword = new JLabel("Password :");
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblPassword.setBounds(21, 58, 171, 26);
        frame.getContentPane().add(lblPassword);

        JLabel lblUrl = new JLabel("Target :");
        lblUrl.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblUrl.setBounds(21, 83, 148, 39);
        frame.getContentPane().add(lblUrl);

        tfusername = new JTextField();
        tfusername.setFont(new Font("Tahoma", Font.PLAIN, 16));
        tfusername.setBounds(112, 18, 199, 32);
        tfusername.setText(userlist.get("username"));
        frame.getContentPane().add(tfusername);
        tfusername.setColumns(10);

        tfpassword = new JPasswordField();
        tfpassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
        tfpassword.setBounds(112, 55, 199, 26);
        tfpassword.setText(userlist.get("password"));
        frame.getContentPane().add(tfpassword);
        tfpassword.setColumns(10);

        tftarget = new JTextField();
        tftarget.setFont(new Font("Tahoma", Font.PLAIN, 16));
        tftarget.setBounds(112, 86, 385, 32);
        tftarget.setText(userlist.get("target"));
//      tftarget.setText("https://www.facebook.com/groups/592918330837781/members/");
        frame.getContentPane().add(tftarget);
        tftarget.setColumns(10);

        JButton btnCencal = new JButton("Cancel Requests");
        btnCencal.setFont(new Font("Source Code Pro", Font.BOLD, 12));
        btnCencal.setBounds(21, 228, 171, 39);
        btnCencal.addActionListener(new ActionListener() {
      	  public void actionPerformed(ActionEvent e) {
      		  try {
				LaunchingChromeCancelRequests();
			} catch (ClassNotFoundException | IOException e1) {}
      	  }
        });
        frame.getContentPane().add(btnCencal);

        JButton btnRun = new JButton("RUN");
        btnRun.setFont(new Font("Source Code Pro", Font.BOLD, 37));
        btnRun.setBounds(216, 191, 281, 76);
        btnRun.addActionListener(new ActionListener() {
        	  public void actionPerformed(ActionEvent e) {
        		  try {
					LaunchingChromeRUN();
				} catch (ClassNotFoundException | IOException e1) {}
        		  }
        });
        frame.getContentPane().add(btnRun);

        JLabel lblCopyright = new JLabel("Copyright\u00A9 Ryan Kwan . klhong124@gmail.com");
        lblCopyright.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblCopyright.setForeground(Color.LIGHT_GRAY);
        lblCopyright.setBounds(21, 276, 275, 22);
        frame.getContentPane().add(lblCopyright);

        JLabel lblFacebook = new JLabel("FACEBOOK");
        lblFacebook.setFont(new Font("Source Code Pro Black", Font.BOLD, 31));
        lblFacebook.setBackground(Color.WHITE);
        lblFacebook.setForeground(new Color(0, 0, 255));
        lblFacebook.setBounds(318, 5, 199, 48);
        frame.getContentPane().add(lblFacebook);

        JLabel lblSpeed = new JLabel("Speed :");
        lblSpeed.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblSpeed.setBounds(21, 151, 148, 39);
        frame.getContentPane().add(lblSpeed);

        tfspeed = new JTextField();
        tfspeed.setFont(new Font("Tahoma", Font.PLAIN, 16));
        tfspeed.setBounds(112, 154, 94, 32);
        tfspeed.setText("3000");
        frame.getContentPane().add(tfspeed);
        tfspeed.setColumns(10);

        JLabel lblAddfdbot = new JLabel("ADDFDBOT");
        lblAddfdbot.setForeground(Color.BLUE);
        lblAddfdbot.setFont(new Font("Source Code Pro Black", Font.BOLD, 31));
        lblAddfdbot.setBackground(Color.WHITE);
        lblAddfdbot.setBounds(318, 46, 182, 41);
        frame.getContentPane().add(lblAddfdbot);

        JLabel lblIdRange = new JLabel("ID range :");
        lblIdRange.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblIdRange.setBounds(21, 118, 148, 39);
        frame.getContentPane().add(lblIdRange);

        tfidrange = new JTextField();
        tfidrange.setText("groupsMemberSection_recently_joined");
        tfidrange.setFont(new Font("Tahoma", Font.PLAIN, 16));
        tfidrange.setColumns(10);
        tfidrange.setBounds(112, 121, 385, 32);
        frame.getContentPane().add(tfidrange);

        JLabel lblNumber = new JLabel("No. :");
        lblNumber.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblNumber.setBounds(359, 151, 123, 39);
        frame.getContentPane().add(lblNumber);

        tfnumber = new JTextField();
        tfnumber.setText(userlist.get("number"));
        tfnumber.setFont(new Font("Tahoma", Font.PLAIN, 16));
        tfnumber.setColumns(10);
        tfnumber.setBounds(403, 154, 94, 32);
        frame.getContentPane().add(tfnumber);

        chckbxRememberMe = new JCheckBox("Remember me");
        if (userlist.get("checkbox").equals("true")) {
        	chckbxRememberMe.setSelected(true);
        }else {chckbxRememberMe.setSelected(false);}
        chckbxRememberMe.setFont(new Font("Tahoma", Font.PLAIN, 16));
        chckbxRememberMe.setBounds(21, 191, 179, 35);
        frame.getContentPane().add(chckbxRememberMe);

        JLabel lblLastestUpdateAugust = new JLabel("Lastest update: August 14");
        lblLastestUpdateAugust.setFont(new Font("Tahoma", Font.ITALIC, 12));
        lblLastestUpdateAugust.setBounds(341, 274, 156, 26);
        frame.getContentPane().add(lblLastestUpdateAugust);

        frame.setVisible(true);
	}

	@SuppressWarnings("deprecation")
	public static void LaunchingChromeRUN() throws ClassNotFoundException, IOException{
		UserInformationSave();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-notifications");
		String exePath = "chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", exePath);
		driver = new ChromeDriver(options);
		driver.get(tftarget.getText());
		WebElement emailelement = driver.findElement(By.id("email"));
		emailelement.clear();
		emailelement.sendKeys(tfusername.getText());
		WebElement passwordelement = driver.findElement(By.id("pass"));
		passwordelement.clear();
		passwordelement.sendKeys(tfpassword.getText());
		WebElement login = driver.findElement(By.id("loginbutton"));
		login.submit();
		int number = Integer.parseInt(tfnumber.getText())-1;
		for (int x = 0; x <= number; x++) {
			while (true) {
				try {
					WebElement addfriendsection = driver.findElement(By.id(tfidrange.getText()));
					ArrayList<WebElement> addfriend = (ArrayList<WebElement>) addfriendsection.findElements(By.className("FriendButton"));
					WebElement facebookid = addfriend.get(x).findElement(By.xpath("../../../../../.."));
					boolean result = checkFacebookID(facebookid.getAttribute("id"));
					if (result) {
						JavascriptExecutor jse = (JavascriptExecutor) driver;
						int sleeptime = (int )(Math.random() * 15 + 1)*100+Integer.parseInt(tfspeed.getText());
						while(true) {
							try {
								if (driver.getCurrentUrl().equals(tftarget.getText())) {
									if(x>1) {
										jse.executeScript("arguments[0].scrollIntoView(true);", addfriend.get(x-2));
									}
								addfriend.get(x).click();
								try {
									Thread.sleep(sleeptime);
								} catch (InterruptedException e1) {}
								break;
								}else {
									driver.get(tftarget.getText());
									addfriendsection = driver.findElement(By.id(tfidrange.getText()));
									addfriend = (ArrayList<WebElement>) addfriendsection.findElements(By.className("FriendButton"));
									facebookid = addfriend.get(x).findElement(By.xpath("../../../../../.."));
								}
							} catch (org.openqa.selenium.WebDriverException e3) {
								for (int i=0; i<4; i++) {
									try {
										switch (i) {
										case 0:	 WebElement Confirmpopup = driver.findElement(By.xpath("//button[contains(.,'Confirm')]"));
												 Confirmpopup.click();
												 break;
										case 1: WebElement Closepopup = driver.findElement(By.xpath("//a[@title='Close']"));
										 		 Closepopup.click();
										 		break;
										case 2: WebElement Closepopup2 = driver.findElement(By.xpath("//a[contains(.,'Close') and @action='cancel']"));
												 Closepopup2.click();
												 break;
										case 3: addfriendsection.click();
												if(x != 0) {
													jse.executeScript("arguments[0].scrollIntoView(true);", addfriend.get(x-1));
												}
												break;
										default: break;
										}
										try {
											Thread.sleep(sleeptime);
										} catch (InterruptedException e1) {}
										break;
									} catch (org.openqa.selenium.NoSuchElementException | org.openqa.selenium.StaleElementReferenceException |  org.openqa.selenium.ElementNotVisibleException e) {}
								}
							}
						}
					}else {
						number = number+1;
					}
					break;
				}catch (java.lang.IndexOutOfBoundsException e) {
					JavascriptExecutor jse = (JavascriptExecutor) driver;
				    jse.executeScript("window.scrollTo(0, document.body.scrollHeight);", "");
				}catch (org.openqa.selenium.StaleElementReferenceException |  org.openqa.selenium.NoSuchElementException e5) {
					driver.get(tftarget.getText());
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void LaunchingChromeCancelRequests() throws ClassNotFoundException, IOException {
		UserInformationSave();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-notifications");
		String exePath = "chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", exePath);
		driver = new ChromeDriver(options);
		driver.get("http://www.facebook.com");
		WebElement emailelement = driver.findElement(By.id("email"));
		emailelement.clear();
		emailelement.sendKeys(tfusername.getText());
		WebElement passwordelement = driver.findElement(By.id("pass"));
		passwordelement.clear();
		passwordelement.sendKeys(tfpassword.getText());
		WebElement login = driver.findElement(By.id("loginbutton"));
		login.submit();
		driver.get("https://m.facebook.com/friends/center/requests/outgoing/#friends_center_main");
		WebElement friends_center_main = driver.findElement(By.id("friends_center_main"));
		while (true) {
			int old_height = friends_center_main.getSize().getHeight();
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("window.scrollTo(0, document.body.scrollHeight);﻿", "");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {}
			if (friends_center_main.getSize().getHeight() == old_height) {
				 for(int i=1; i<10; i++){
					jse.executeScript("window.scrollTo(0, document.body.scrollHeight);﻿", "");
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {}
				 }
				 if (friends_center_main.getSize().getHeight() == old_height) {
					jse.executeScript("var inputs = document.getElementsByClassName('_54k8 _56bs _56bt'); for(var i=0; i<inputs.length;i++) { inputs[i].click();}", "");
					break;
				 }
			}
		}
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException {
			AddFriendBot.LaunchGUIPanel();
	}
}
