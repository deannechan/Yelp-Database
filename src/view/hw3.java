package view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.toedter.calendar.JDateChooser;

import model.DBQueries;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.LineBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class hw3 extends JFrame {

	private JPanel contentPane;
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private JTextField numCheckInField;
	private JTextField starsField;
	private JTextField votesField;
	private JTextField textField;
	private JTextField userReviewCount;
	private JTextField userFriendCount;
	private JTextField userStars;
	private JScrollPane categoriesPane;
	private JScrollPane subcategoriesPane;
	private JPanel categoryPanel;
	private JPanel subcategoryPanel;
	private JPanel checkinPanel;
	private JComboBox ci_fromDayComboBox;
	private JComboBox ci_fromHourComboBox;
	private JComboBox ci_toDayComboBox;
	private JComboBox ci_toHourComboBox;
	private JComboBox numCheckinComboBox;
	private JDateChooser reviewFromDate;
	private JDateChooser reviewToDate;
	private JComboBox starsComboBox;
	private JComboBox votesComboBox;
	private JScrollPane resultPane;
	private JTextArea queryTextArea;
	private JTable table = new JTable();
	private JDateChooser memberSinceDate;
	private JComboBox userReviewComboBox;
	private JComboBox userFriendComboBox;
	private JComboBox userStarsComboBox;
	private JComboBox AndOrcomboBox;
	
	private int tab = 0;
	private List<JCheckBox> categoriesCheckboxes = new ArrayList<>();
	private List<JCheckBox> subcategoriesCheckboxes = new ArrayList<>();
	private ActionListener categoryListener;
	private ActionListener dayComboListener;
	private ActionListener hourComboListener;
	private ActionListener executeQueryListener;
	
	private DBQueries database = new DBQueries();
	
	private HashMap<Integer, ArrayList<Integer>> times;
	private HashMap<String, Integer> daysConversion = new HashMap<>();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					hw3 frame = new hw3();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public hw3() {
		setBackground(new Color(173, 216, 230));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1071, 672);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(245, 245, 245));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		tabbedPane.setBackground(new Color(248, 248, 255));

////// business panel
		JPanel businessPanel = new JPanel();
		businessPanel.setBackground(new Color(248, 248, 255));
		tabbedPane.addTab("Business", null, businessPanel, null);
		businessPanel.setLayout(null);

////// categories panel, categories panel
		categoriesPane = new JScrollPane();
		categoriesPane.setBounds(6, 6, 217, 262);
		businessPanel.add(categoriesPane);
		
		JLabel lblCategories = new JLabel("Categories", SwingConstants.CENTER);
		categoriesPane.setColumnHeaderView(lblCategories);
		
		categoryPanel = new JPanel();
		categoryPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		categoryPanel.setBackground(Color.WHITE);
		categoriesPane.setViewportView(categoryPanel);
		
		categoryListener = new categoryListener();
				
		tabbedPane.addChangeListener(new ChangeListener() {
	        public void stateChanged(ChangeEvent e) {
	        	tab = tabbedPane.getSelectedIndex();
	            System.out.println("Tab: " + tab);
	            clearResults();
    			
	            if(tab == 0){
	            	categoryPanel.removeAll();	
	            	subcategoryPanel.removeAll();
	            	ArrayList<String> b_cat = database.getCategories();
	        		BoxLayout bl_categoryPanel = new BoxLayout(categoryPanel, BoxLayout.Y_AXIS);
	        		categoryPanel.setLayout(bl_categoryPanel);
	        		categoriesCheckboxes = new ArrayList<>();
	                for (String string : b_cat) {
	                	JCheckBox catCheckbox = new JCheckBox(string);
	                	catCheckbox.setName(string);
	                	catCheckbox.addActionListener(categoryListener);
	                	categoriesCheckboxes.add(catCheckbox);
	                    categoryPanel.add(catCheckbox);
	                }
	                categoriesPane.setViewportView(categoryPanel);
	                updateCheckInPanel();
	                reviewFromDate.setCalendar(null);
	                reviewToDate.setCalendar(null);
	                starsComboBox.setSelectedIndex(0);
	                starsField.setText("");
	                votesComboBox.setSelectedIndex(0);
	                votesField.setText("");
	           
	            } else if(tab == 1){
	            	userReviewComboBox.setSelectedIndex(0);
	        		userFriendComboBox.setSelectedIndex(0);
	        		userStarsComboBox.setSelectedIndex(0);
	        		AndOrcomboBox.setSelectedIndex(0);
	        		
	        		memberSinceDate.setCalendar(null);
	        		userReviewCount.setText("");
	        		userFriendCount.setText("");
	        		userStars.setText("");
	            }
	        }
	    });
		
		ArrayList<String> b_cat = database.getCategories();
		BoxLayout bl_categoryPanel = new BoxLayout(categoryPanel, BoxLayout.Y_AXIS);
		categoryPanel.setLayout(bl_categoryPanel);
		categoriesCheckboxes = new ArrayList<>();
        for (String string : b_cat) {
        	JCheckBox catCheckbox = new JCheckBox(string);
        	catCheckbox.setName(string);
        	catCheckbox.addActionListener(categoryListener);
        	categoriesCheckboxes.add(catCheckbox);
            categoryPanel.add(catCheckbox);
        }
        categoriesPane.setViewportView(categoryPanel);

////// subcategories pane, subcategories panel
		subcategoriesPane = new JScrollPane();
		subcategoriesPane.setBounds(223, 6, 217, 262);
		businessPanel.add(subcategoriesPane);
		
		JLabel lblSubcategories = new JLabel("Subcategories", SwingConstants.CENTER);
		subcategoriesPane.setColumnHeaderView(lblSubcategories);
		
		subcategoryPanel = new JPanel();
		subcategoryPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		subcategoryPanel.setBackground(Color.WHITE);
		subcategoriesPane.setViewportView(subcategoryPanel);
		
//// check in panel
		checkinPanel = new JPanel();
		checkinPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		checkinPanel.setBackground(new Color(255, 255, 255));
		checkinPanel.setBounds(8, 271, 215, 268);
		businessPanel.add(checkinPanel);
		checkinPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("61px"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("59px:grow"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("16px"),
				FormSpecs.PARAGRAPH_GAP_ROWSPEC,
				RowSpec.decode("16px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JLabel lblCheckin = new JLabel("Check-In", SwingConstants.CENTER);
		checkinPanel.add(lblCheckin, "2, 2, 5, 1, fill, top");
		
		JLabel lblFrom = new JLabel("From");
		checkinPanel.add(lblFrom, "2, 4, left, top");
		
		JLabel lblDay = new JLabel("Day",SwingConstants.RIGHT);
		checkinPanel.add(lblDay, "2, 6, right, default");
		
		JLabel lblHour = new JLabel("Hour",SwingConstants.RIGHT);
		checkinPanel.add(lblHour, "2, 8, right, default");
		
		JLabel lblTo = new JLabel("To");
		checkinPanel.add(lblTo, "2, 10");
		
		JLabel lblDay_1 = new JLabel("Day",SwingConstants.RIGHT);
		checkinPanel.add(lblDay_1, "2, 12, right, default");
		
		JLabel lblHour_1 = new JLabel("Hour",SwingConstants.RIGHT);
		checkinPanel.add(lblHour_1, "2, 14, right, default");
		
		JLabel lblNumberOfCheckins = new JLabel("Num of checkins");
		checkinPanel.add(lblNumberOfCheckins, "2, 16, 3, 1");
		
		ci_fromDayComboBox = new JComboBox();
		ci_fromDayComboBox.setName("fromDay");
		ci_toDayComboBox = new JComboBox();
		ci_toDayComboBox.setName("toDay");
		
		ci_fromHourComboBox = new JComboBox();
		ci_fromHourComboBox.setName("fromHour");
		ci_toHourComboBox = new JComboBox();
		ci_toHourComboBox.setName("toHour");
		
		checkinPanel.add(ci_fromDayComboBox, "4, 6, 3, 1, fill, default");
		checkinPanel.add(ci_fromHourComboBox, "4, 8, 3, 1, fill, default");
		checkinPanel.add(ci_toDayComboBox, "4, 12, 3, 1, fill, default");
		checkinPanel.add(ci_toHourComboBox, "4, 14, 3, 1, fill, default");
		
		dayComboListener = new dayComboListener();
		ci_fromDayComboBox.addActionListener(dayComboListener);
		ci_toDayComboBox.addActionListener(dayComboListener);
//		ci_fromHourComboBox.addActionListener(hourComboListener);
//		ci_toHourComboBox.addActionListener(hourComboListener);
		
		numCheckinComboBox = new JComboBox();
		numCheckinComboBox.setModel(new DefaultComboBoxModel(new String[] {"<", "=", ">"}));
		checkinPanel.add(numCheckinComboBox, "4, 18, fill, default");
		
		numCheckInField = new JTextField();
		checkinPanel.add(numCheckInField, "5, 18, 2, 1, fill, center");
		numCheckInField.setColumns(10);
		
		updateCheckInPanel();
////// reviews panel
		JPanel reviewsPanel = new JPanel();
		reviewsPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		reviewsPanel.setBackground(new Color(255, 255, 255));
		reviewsPanel.setBounds(223, 271, 215, 268);
		businessPanel.add(reviewsPanel);
		reviewsPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("50px"),
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.UNRELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("16px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));		

		JLabel lblReviews = new JLabel("Reviews", SwingConstants.CENTER);
		reviewsPanel.add(lblReviews, "1, 2, 5, 1, fill, top");
		
		JLabel lblFrom_1 = new JLabel("From");
		reviewsPanel.add(lblFrom_1, "2, 4");
		
		JLabel lblTo_1 = new JLabel("To");
		reviewsPanel.add(lblTo_1, "2, 6");
		
		JLabel lblStars = new JLabel("Stars");
		reviewsPanel.add(lblStars, "2, 10");
		
		reviewFromDate = new JDateChooser();
		reviewFromDate.setLocale(Locale.US);
		reviewsPanel.add(reviewFromDate, "3, 4, 3, 1");
		
		reviewToDate = new JDateChooser();
		reviewToDate.setLocale(Locale.US);
		reviewsPanel.add(reviewToDate, "3, 6, 3, 1");
		
		starsComboBox = new JComboBox();
		starsComboBox.setModel(new DefaultComboBoxModel(new String[] {"<", "=", ">"}));
		reviewsPanel.add(starsComboBox, "3, 12, fill, default");
		
		starsField = new JTextField();
		reviewsPanel.add(starsField, "5, 12, fill, top");
		starsField.setColumns(10);
		
		JLabel lblVotes = new JLabel("Votes");
		lblVotes.setBackground(new Color(255, 255, 255));
		reviewsPanel.add(lblVotes, "2, 16");
		
		votesComboBox = new JComboBox();
		votesComboBox.setModel(new DefaultComboBoxModel(new String[] {"<", "=", ">"}));
		reviewsPanel.add(votesComboBox, "3, 18, fill, default");
		
		votesField = new JTextField();
		reviewsPanel.add(votesField, "5, 18, fill, top");
		votesField.setColumns(10);
		
////// user panel		
		JPanel userPanel = new JPanel();
		userPanel.setBackground(new Color(255, 255, 255));
		tabbedPane.addTab("User", null, userPanel, null);
		userPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JLabel lblMembersSince = new JLabel("Members Since");
		userPanel.add(lblMembersSince, "4, 4");
		
		JLabel lblReviewCount = new JLabel("Review count");
		userPanel.add(lblReviewCount, "4, 8");
		
		JLabel lblNumOfFriends = new JLabel("Num of friends");
		userPanel.add(lblNumOfFriends, "4, 12");
		
		JLabel lblNewLabel = new JLabel("Average stars");
		userPanel.add(lblNewLabel, "4, 16");
		
		JLabel lblSelect = new JLabel("Select");
		userPanel.add(lblSelect, "4, 20");
		
		JLabel lblBetweenAttributes = new JLabel("between attributes");
		userPanel.add(lblBetweenAttributes, "10, 20");
		
		memberSinceDate = new JDateChooser();
		memberSinceDate.setLocale(Locale.US);
		userPanel.add(memberSinceDate, "8, 4, 3, 1");
		
		userReviewComboBox = new JComboBox();
		userReviewComboBox.setModel(new DefaultComboBoxModel(new String[] {"=", ">", "<"}));
		userPanel.add(userReviewComboBox, "8, 8, left, default");
		
		userReviewCount = new JTextField();
		userPanel.add(userReviewCount, "10, 8, left, default");
		userReviewCount.setColumns(5);
		
		userFriendComboBox = new JComboBox();
		userFriendComboBox.setModel(new DefaultComboBoxModel(new String[] {"=", ">", "<"}));
		userPanel.add(userFriendComboBox, "8, 12, left, default");
		
		userFriendCount = new JTextField();
		userPanel.add(userFriendCount, "10, 12, left, default");
		userFriendCount.setColumns(5);
		
		userStarsComboBox = new JComboBox();
		userStarsComboBox.setModel(new DefaultComboBoxModel(new String[] {"=", ">", "<"}));
		userPanel.add(userStarsComboBox, "8, 16, left, default");
		
		userStars = new JTextField();
		userPanel.add(userStars, "10, 16, left, default");
		userStars.setColumns(5);
		
		AndOrcomboBox = new JComboBox();
		AndOrcomboBox.setModel(new DefaultComboBoxModel(new String[] {"AND", "OR"}));
		userPanel.add(AndOrcomboBox, "8, 20");
		
///// result pane
		resultPane = new JScrollPane();
		
		executeQueryListener = new executeQueryListener();
		JButton btnExecuteQuery = new JButton("Execute Query");
		btnExecuteQuery.addActionListener(executeQueryListener);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JLabel lblResults = new JLabel("Results", SwingConstants.CENTER);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 465, GroupLayout.PREFERRED_SIZE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 572, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(222)
							.addComponent(btnExecuteQuery, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(resultPane, GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED))
								.addComponent(lblResults, GroupLayout.PREFERRED_SIZE, 575, GroupLayout.PREFERRED_SIZE))))
					.addGap(1))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(28)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(lblResults)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(resultPane, GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 139, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnExecuteQuery)
							.addGap(15))
						.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 591, GroupLayout.PREFERRED_SIZE)))
		);
		
//		JPanel resultsPanel = new JPanel();
//		resultsPanel.setBackground(new Color(255, 255, 255));
//		resultPane.setViewportView(resultsPanel);
		
		queryTextArea = new JTextArea();
		scrollPane.setViewportView(queryTextArea);
		queryTextArea.setLineWrap(true);
		queryTextArea.setWrapStyleWord(true);
		contentPane.setLayout(gl_contentPane);
		
//////////////////////////////////////////
		daysConversion.put("Sunday", 0);
		daysConversion.put("Monday", 1);
		daysConversion.put("Tuesday", 2);
		daysConversion.put("Wednesday", 3);
		daysConversion.put("Thursday", 4);
		daysConversion.put("Friday", 5);
		daysConversion.put("Saturday", 6);
		
		table.addMouseListener(new MouseListener(){
            @Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.println(table.getValueAt(table.getSelectedRow(), 0).toString());
				
                String id = table.getValueAt(table.getSelectedRow(), 0).toString();
                DefaultTableModel model = null;
                if(tab == 0){
                	model = database.getReviewsFromBusiness(id);
                	queryTextArea.append(System.lineSeparator());
                    queryTextArea.append(System.lineSeparator());
                    queryTextArea.append(database.createReviewBusinessQuery(id));
                } else if(tab == 1){
                	model = database.getReviewsFromUser(id);
                	queryTextArea.append(System.lineSeparator());
                    queryTextArea.append(System.lineSeparator());
                    queryTextArea.append(database.createReviewUserQuery(id));
                }
                
                //make frame for reviews
                JFrame reviewFrame = new JFrame();
                JTable reviewTable = new JTable();
                reviewTable.setModel(model);
                JScrollPane reviewScrollPane = new JScrollPane(reviewTable);
                reviewFrame.getContentPane().add(reviewScrollPane, BorderLayout.CENTER);
                reviewFrame.setSize(800, 500);
                reviewFrame.setVisible(true);
            	
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

        });
	}
	
	public ArrayList<String> getCatSelectedCheckboxes(){
	    ArrayList<String> categoriesList = new ArrayList<String>();
	    for (JCheckBox catCheckbox : categoriesCheckboxes) {
	        if (catCheckbox.isSelected()) {
	        	categoriesList.add(catCheckbox.getText());
	        }
	    }
		return categoriesList;
	}
	
	public ArrayList<String> getSubcatSelectedCheckboxes(){
	    ArrayList<String> subcategoriesList = new ArrayList<String>();
		for (JCheckBox subcatCheckbox : subcategoriesCheckboxes) {
			if (subcatCheckbox.isSelected()) {
		       subcategoriesList.add(subcatCheckbox.getText());
		    }
		}
		return subcategoriesList;
	}
	
	public void clearResults(){
		queryTextArea.setText("");
//		DefaultTableModel model = (DefaultTableModel) table.getModel();
//		model.setRowCount(0);
		table.setModel(new DefaultTableModel());
	}
	
	private class categoryListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			JCheckBox source = (JCheckBox)event.getSource();
			String clicked = source.getName();
			updateSubcategoryPanel(getCatSelectedCheckboxes());
			updateCheckInPanel();
			clearResults();
		}
	}
	
	private class dayComboListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			String day = (String) ((JComboBox)event.getSource()).getSelectedItem();
			JComboBox combobx = ((JComboBox) event.getSource());
				
			JComboBox toUpdate = null;
			if(combobx.getName().equals("fromDay")){
				toUpdate = ci_fromHourComboBox;
			} else {
				toUpdate = ci_toHourComboBox;
			}
			toUpdate.removeAllItems();
			if(day != null && !day.equals("Days")){
				toUpdate.setModel(updateHourComboBox(times, day));
				toUpdate.setSelectedIndex(0);
			} else{
				toUpdate.setModel(new DefaultComboBoxModel(new String[] {"Hours"}));
			}
		}
	}
	
	private class executeQueryListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
			if(tab == 0) { // business
				ArrayList<String> categoriesList = getCatSelectedCheckboxes();
				ArrayList<String> subcategoriesList = getSubcatSelectedCheckboxes();
				
				String from_day = (String) ci_fromDayComboBox.getSelectedItem();
				String to_day = (String) ci_toDayComboBox.getSelectedItem();
				String from_hour = (String) ci_fromHourComboBox.getSelectedItem();
				String to_hour = (String) ci_toHourComboBox.getSelectedItem();
		
				String numCheckinOperator = (String) numCheckinComboBox.getSelectedItem();
				String numCheckin = numCheckInField.getText();
				
				Date from_review = (Date) reviewFromDate.getDate();
				Date to_review = (Date) reviewToDate.getDate();
				String b_starsOperator = (String) starsComboBox.getSelectedItem();
				String b_stars = starsField.getText();
				String b_votesOperator = (String) votesComboBox.getSelectedItem();
				String b_votes = votesField.getText();
				
				String q;
				q = database.getFullBusinessQuery(categoriesList, subcategoriesList, from_day, to_day,
						from_hour, to_hour, numCheckinOperator, numCheckin, from_review, to_review,  b_starsOperator,
						b_stars, b_votesOperator, b_votes);
	            queryTextArea.setText(q);
	            
				DefaultTableModel d = database.getBusinessesQuery(categoriesList, subcategoriesList, from_day, to_day,
						from_hour, to_hour, numCheckinOperator, numCheckin, from_review, to_review,  b_starsOperator,
						b_stars, b_votesOperator, b_votes);

				table.setModel(d);
	            resultPane.setViewportView(table);
	    		
			} else if(tab == 1) { // user
				String reviewOp = (String) userReviewComboBox.getSelectedItem();
				String reviewCnt = (String) userReviewCount.getText();	
				String friendOp = (String) userFriendComboBox.getSelectedItem();
				String friendCnt = (String) userFriendCount.getText();
				String starOp = (String) userStarsComboBox.getSelectedItem();
				String starCnt = (String) userStars.getText();
				Date startMember = memberSinceDate.getDate(); // get month & year
        		String andOr = (String) AndOrcomboBox.getSelectedItem();
        		
				String q = "";
				try {
					q = database.createUserQuery(reviewOp, reviewCnt, friendOp, friendCnt, starOp, starCnt, andOr, startMember);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
	            queryTextArea.setText(q);
	            
	            DefaultTableModel d = database.getUsersQuery(reviewOp, reviewCnt, friendOp, friendCnt, starOp, starCnt, andOr, startMember);
				table.setModel(d);
	            resultPane.setViewportView(table);
	           
			}
		}
	}
	
	public void updateSubcategoryPanel(ArrayList<String> categoriesChecked){
		subcategoryPanel.removeAll();
		BoxLayout bl_categoryPanel = new BoxLayout(subcategoryPanel, BoxLayout.Y_AXIS);
		subcategoryPanel.setLayout(bl_categoryPanel);
		
		ArrayList<String> subcategories = database.getSubcategories(categoriesChecked);
		subcategoriesCheckboxes = new ArrayList<>();
        for (String string : subcategories) {
        	JCheckBox subcatCheckbox = new JCheckBox(string);
        	subcatCheckbox.setName(string);
        	subcatCheckbox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					JCheckBox source = (JCheckBox)event.getSource();
					String clicked = source.getName();
//					System.out.println(clicked);
					
					updateCheckInPanel();
				}
        		
        	});
        	subcategoriesCheckboxes.add(subcatCheckbox);
            subcategoryPanel.add(subcatCheckbox);
        }
        subcategoriesPane.setViewportView(subcategoryPanel);
	}
	
	public void updateCheckInPanel(){
//		dayComboListener = new dayComboListener();
		
		times = database.getCheckInTimes(getCatSelectedCheckboxes(), getSubcatSelectedCheckboxes());
		
		// get days for businesses
		String[] convertToDay = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
		String[] days = new String[times.size()+1];
		days[0] = "Days";
		int idx=1;
		for(Integer d:times.keySet()){
			days[idx++] = convertToDay[d];
		}
		
		ci_fromDayComboBox.removeAllItems();
		ci_toDayComboBox.removeAllItems();
		
		ci_fromDayComboBox.setModel(new DefaultComboBoxModel(days));
		ci_toDayComboBox.setModel(new DefaultComboBoxModel(days));
		
		ci_fromDayComboBox.setSelectedIndex(0);
		ci_toDayComboBox.setSelectedIndex(0);
		
		ci_fromHourComboBox.removeAllItems();
		ci_toHourComboBox.removeAllItems();
		
		ci_fromHourComboBox.setModel(new DefaultComboBoxModel(new String[] {"Hours"}));
		ci_toHourComboBox.setModel(new DefaultComboBoxModel(new String[] {"Hours"}));
		
		numCheckinComboBox.setSelectedIndex(0);
		numCheckInField.setText("");
	}
	
	public DefaultComboBoxModel updateHourComboBox(HashMap<Integer, ArrayList<Integer>> times, String day){
		Set<Integer> keys = times.keySet();
		ArrayList<Integer> dayTimes = times.get(daysConversion.get(day));

		int minHour = Collections.min(dayTimes);
		int maxHour = Collections.max(dayTimes);

		String[] hours = new String[dayTimes.size()+1];
		hours[0] = "Hours";
		int idx = 1;
		for(Integer t:dayTimes){
			hours[idx++] = t+":00";
//			if(t == 23){
//				hours[idx++] = "23:00-0:00";
//			} else {
//				hours[idx++] = t+":00-"+(t+1)+":00";
//			}
		}
		return new DefaultComboBoxModel(hours);
	}
}
