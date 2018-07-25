package fm.doe.national.ui.screens.main;

import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data.converters.JsonImporter;
import fm.doe.national.data.data_source.db.DbAccreditationDataSource;
import fm.doe.national.ui.screens.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Inject
    DbAccreditationDataSource staticDataSource;

    @Inject
    JsonImporter jsonImporter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MicronesiaApplication.getAppComponent().inject(this);
        /*staticDataSource.requestSchools().subscribe(schools -> Log.d("TAG", "Size = " + schools
                .size()));*/
        jsonImporter.importData("{\n" +
                "  \"group_standard\": [\n" +
                "    {\n" +
                "      \"name\": \"Leadership\",\n" +
                "      \"standard\": [\n" +
                "        {\n" +
                "          \"name\": \"Leadership\",\n" +
                "          \"criteria\": [\n" +
                "            {\n" +
                "              \"name\": \"The principal has very high expectations for students and teachers.\",\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"The principal ensures that a minimum of 180 instructional days are delivered and can provide documentary evidence.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The mandatory number of instructional hours is delivered (Grade 1&2: 3 hours and 36 minutes: Grades 3-12: 4 hours 15 minutes per day).\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Corporal punishment is not allowed.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Students are responsible and student behavior is very orderly and respectful towards adults and other students.\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"The principal has a clear philosophy of education and is an expert in curriculum and instruction. \",\n" +
                "              \"weightage\": 4,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"The principal manages a program of teacher collaboration for instructional planning.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The principal conducts weekly planning meetings with teachers and staff to develop and review curriculum, syllabi, programs of study, and lesson plans and\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The principal ensures that there is continuity and progression among students between grades in the school.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The Principal has a written Educational Philosophy posted in his office and all classrooms.\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"The principal is involved in a program of continuous professional development and appraisal by supervisors, board members or peers. \",\n" +
                "              \"weightage\": 6,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"The principal keeps up to date with innovations in education.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The principal creates many opportunities for teachers to engage in continuous professional development activities.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The principal mentors teachers and conducts formal and informal training at the school.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The principal is evaluated annually by their supervisor/s\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"The principal conducts a regular program of teacher performance appraisals.\",\n" +
                "              \"weightage\": 6,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"There are regular, structured classroom observation and written reports are produced.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Feedback on observations is constructive and designed to improve teaching and learning.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The principal coaches teachers and conducts model lessons where necessary.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The principal encourages teachers to evaluate their own performance based on student learning outcomes.\"\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Teacher Performance\",\n" +
                "      \"standard\": [\n" +
                "        {\n" +
                "          \"name\": \"Teacher Performance\",\n" +
                "          \"criteria\": [\n" +
                "            {\n" +
                "              \"name\": \"All teachers prepare standardized long, medium or short-term lesson plans that provide adequate guidance for teaching and learning activities.\",\n" +
                "              \"weightage\": 5,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"Plans include appropriate learning activities including differentiated activities for different learners, including those with IEPs.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"All plans include sequenced, measurable learning objectives from the National and/or State Curricula.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Assessment is planned and integrated into lessons and is based on observable and measurable criteria.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Student assessment records are used for planning purposes.\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"All teachers create high quality classroom learning environments.\",\n" +
                "              \"weightage\": 5,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"The classrooms and other teaching areas are rich in print and visual displays that are related to the current learning objectives.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"There are designated areas of the classrooms with topical displays.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Book corners or activity centers have appropriate learning materials.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"A wide range of student work of very high quality is neatly displayed and clearly labeled.\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"All teachers manage their time effectively to maximize students’ learning opportunities.\",\n" +
                "              \"weightage\": 5,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"All lessons start and finish on time.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Students are on-task and engaged in learning activities for most of the lesson time.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"A variety of teaching and learning strategies are used, including strategies suitable for students with IEPs.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Student work demonstrates higher-order thinking, inquiry, observation, analysis and problem solving skills.\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"All teachers actively participate with enthusiasm in a school-based appraisal program.\",\n" +
                "              \"weightage\": 5,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"All teachers, individually or in groups, reflect upon and evaluate their performance.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"All teachers participate in professional development.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Teachers regularly make thoughtful and accurate written evaluations of lesson effectiveness based on children’s learning outcomes.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"All teachers engage in a formal means of peer observation.\"\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Data Management\",\n" +
                "      \"standard\": [\n" +
                "        {\n" +
                "          \"name\": \"Data Management\",\n" +
                "          \"criteria\": [\n" +
                "            {\n" +
                "              \"name\": \"Comprehensive and accurate school data is collected on a regular schedule.\",\n" +
                "              \"weightage\": 5,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"Complete and accurate school data is collected on a quarterly schedule.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Daily attendance data for students and teachers is collected and analyzed to produce daily, weekly and annual raw number reports for individuals, classes and the whole school.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Reports for the last three years or more are available. \"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Action is taken early to address attendance and drop-out issues, and no student in the mandatory education range drops out of formal education.\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"All school data is centrally located and carefully filed.\",\n" +
                "              \"weightage\": 3,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"All SDOE-mandated school data files are backed-up or duplicated and are securely held, including IEPs.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"All data is checked, cleaned and updated at least weekly.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Data is provided to the Department of Education by September 15th in either electronic or hardcopy format.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"All school data is centrally located and accessible.\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"All student performance data, including classroom assessment, teacher observations, student portfolios and test data, is analyzed by all teachers to identify individual students’ strengths and weaknesses, including students with IEPs.\",\n" +
                "              \"weightage\": 4,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"Scheduled assessment meetings are held by teacher teams at least once per quarter.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Data analysis is used by teachers to adapt their teaching strategies and programs to meet individual student needs.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Teacher observations are used by all teachers to identify students’ strengths and weaknesses, including students with IEPs.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Classroom assessment, including student portfolios, are analyzed by all teachers to identify students’ strengths and weaknesses, including students with IEPs.\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"An extensive collection of school data is used to guide all management decision making.\",\n" +
                "              \"weightage\": 3,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"An extensive collection of school data is used to guide all management and resource decision making, with at least two examples given during the Principal Interview of decisions made because of data.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"A full range of student performance and attendance data, including findings from surveys, is shown and used in the School Improvement Plan.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Data analysis is used by the principal to support teacher deployment, with at least two decisions expanded upon during the Principal Interview.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Data analysis is used by the principal to support student placement, with at least two decisions expanded upon during the Principal Interview.\"\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"National Curriculum Standards, Benchmarks and Student Learning Outcomes\",\n" +
                "      \"standard\": [\n" +
                "        {\n" +
                "          \"name\": \"National Curriculum Standards, Benchmarks and Student Learning Outcomes\",\n" +
                "          \"criteria\": [\n" +
                "            {\n" +
                "              \"name\": \"Written school policy states that English Language Arts, Vernacular Language Arts, Mathematics and Science are taught throughout the school in accordance with the National Curriculum.\",\n" +
                "              \"weightage\": 2,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"Written school policy states that English Language Arts, Vernacular Language Arts, Mathematics, Social Studies, and Science are taught throughout the school in accordance with the National Curriculum and/or State Curriculum.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"All lesson plans are based on the National or State Curriculum Standards, Benchmarks and Student Learning Outcomes.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Classroom schedules follow the National and/or State adopted Core Classes.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"All lesson plans include the SDOE-required components.\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"Student performance data shows that the vast majority of students reach Competent or Minimum Competent levels on the NMCT and pass other State-mandated exams.\",\n" +
                "              \"weightage\": 5,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"75% of benchmarks in all tested English Language Arts NMCTs reached the Competent or Minimally Competent category.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"75% of benchmarks in all tested Mathematics NMCTs reached the Competent or Minimally Competent category.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The average student score in all tested Mathematics NMCTs is 75% of questions correct or more.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The average student score in all tested English NMCTs is 75% of questions correct or more.\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"All students in all grades have their own copies of modern textbooks for Vernacular Language Arts, English Language Arts, Mathematics, Science and Social Studies.\",\n" +
                "              \"weightage\": 5,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"All students in all grades have their own copies of modern (and/or any) textbooks for Vernacular Language Arts, English Language Arts, Mathematics, Science and Social Studies.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"All textbooks are aligned to the correct grade level or students are working above grade level and there is clear progression from one grade to the next.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Students have access to additional resources, including on-line resources, in a well-stocked library, and/or traditional knowledge for schools.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"All teachers have a Teacher’s Guide/Teacher’s Edition for all grades and subject areas taught.\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"A full timetable of extra-curricular academic, sports, cultural, civic and community activities are conducted after school for both boys and girls.\",\n" +
                "              \"weightage\": 3,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"At least two extra-curricular academic, sports, conducted after school for both boys and girls.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The school informs students of, and promotes cultural, civic and community activities.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Students have received awards for extra-curricular activities from local and international bodies and organizations.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Values such as fair play and healthy lifestyles are promoted.\"\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"School Campus, Classrooms and Facilities\",\n" +
                "      \"standard\": [\n" +
                "        {\n" +
                "          \"name\": \"School Campus, Classrooms and Facilities\",\n" +
                "          \"criteria\": [\n" +
                "            {\n" +
                "              \"name\": \"The school has a written maintenance and facilities development plan.\",\n" +
                "              \"weightage\": 3,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"The school has a written maintenance plan that sets out a schedule of routine cleaning, minor repairs, painting and preventative maintenance.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Records show that maintenance work is regularly carried out.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The school has a written facilities development plan that sets out a schedule for major maintenance works, refurbishment or rebuilding.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Records and plans show that the facilities development plan is actively implemented.\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"The campus is maintained to a very high standard of cleanliness, free of litter, graffiti and vandalism, and are explicitly drug-free zones.\",\n" +
                "              \"weightage\": 5,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"Buildings are neat, clean, safe and secure (inside-out and outside-in), free of graffiti (e.g. writing on walls) vandalism (i.e. destruction of government property), and are explicitly drug-free zones.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The school compound is neat, clean, safe and secure, free of graffiti (e.g. writing on walls), vandalism (i.e. destruction of government property), and is explicitly a drug-free zone.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"All school facilities are easily accessible to students with physical disabilities (inside-out and outside-in).\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The campus is used as a learning resource, with a school garden, and sports and play facilities.\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"Classrooms meet recommended area requirements, with adequate lighting and ventilation.\",\n" +
                "              \"weightage\": 3,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"All classrooms used by groups of 30 or more students exceed 25 feet by 25 feet in area, and classrooms used by less than 30 students allow in excess of 20 square feet of floor space per student.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"All classrooms are bright, well ventilated and equipped with adequate lighting and electrical outlets and all electrical fittings are in as-new condition with safety cut-off switches or circuit breakers, and all electrical fittings are in as-new condition with safety cut-off switches or circuit breakers.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"All classrooms have high-quality student and teacher furniture, including adequate storage and display.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"If there are science labs, they have sinks with running water.\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"Food storage, preparation and dining areas are modern and clean, with a current EPA certificate clearly posted; the school has an emergency evacuation plan with evidence of a drill.\",\n" +
                "              \"weightage\": 4,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"All food handling and preparation personnel wear protective clothing, are trained and certified by the Public Health Department and their certificates are clearly displayed.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Toilets have a current EPA certificate verifying that they are safe and clean.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"There is adequate safe drinking water.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"There is an emergency evacuation plan and evidence of a drill within the past 3 months.\"\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"School Improvement Planning\",\n" +
                "      \"standard\": [\n" +
                "        {\n" +
                "          \"name\": \"School Improvement Planning\",\n" +
                "          \"criteria\": [\n" +
                "            {\n" +
                "              \"name\": \"The School Improvement Plan has been developed and evaluated in accordance with the National Guidelines.\",\n" +
                "              \"weightage\": 5,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"The School Improvement Plan has been developed by a school improvement team that includes all teachers, and selected parents, students, community members and other stakeholders who wish to be involved.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The SIP is reviewed and revised on an annual basis.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"There is evidence of improving student achievement as a result of the SIP.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"All goals in the SIP are student achievement oriented.\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"The school principal plays a leadership role in the development and implementation of the SIP.\",\n" +
                "              \"weightage\": 3,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"The SIP contains a Vision and Mission, the former successfully stating what the school wants to become, and the latter successfully stating what they do and how they do it.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The Vision and Mission are well known and shared by the school community.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The SIP is effectively implemented and monitored.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"All staff members and members of the school community are fully committed to implementing the SIP.\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"The School Improvement Plan contains comprehensive data analysis of student performance, attendance, resources and school management, over a period of at least three years, noting school strengths and weaknesses.\",\n" +
                "              \"weightage\": 4,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"The School Improvement Plan contains three years of comprehensive data analysis of student performance, presented graphically\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The School Improvement Plan contains three years of comprehensive data analysis of student attendance, presented graphically\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The School Improvement Plan contains three years comprehensive data analysis of school resources, presented graphically\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The School Improvement Plan contains three years comprehensive data analysis of school management, presented graphically\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"The School Improvement Plan contains SMART Student Achievement Goals and Objectives and Activity Planning Matrices for all activities, with inputs, timelines, outputs and outcomes.\",\n" +
                "              \"weightage\": 3,\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"The SIP is posted in a public place and regular monitoring reports are issued to the community.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Financial records for the last three years are extensive, accurate and transparent.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The SIP contains SMART Student Achievement Goals and Objectives and Activity Planning Matrices for all activities, with inputs, timelines, outputs and outcomes.\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"At least 90% of SIP goals are being achieved.\"\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Classroom Observation\",\n" +
                "      \"standard\": [\n" +
                "        {\n" +
                "          \"name\": \"Planning and preparation\",\n" +
                "          \"criteria\": [\n" +
                "            {\n" +
                "              \"name\": \"Long term plans show an understanding of the curriculum, the connections between different subject areas and an attempt to link the curriculum to local contexts\",\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"There is evidence that National and State curriculum documents and guidelines are regularly used in planning\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"There is a written program for all curriculum areas for a period of at least 4 weeks\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The program identifies major concepts and breaks them down into sequenced learning steps and appropriate learning activities\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Some learning objectives are integrated in themes or topics and the use of Information and Communications Technology is integrated\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The program is linked to local events and seasonal activities\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"Short term lesson plans provide adequate guidance for teaching and learning activities\",\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"Written plans for at least 1 week show continuity and progression between lessons\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Plans include sequenced, measurable learning objectives from the curriculum\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Plans include appropriate learning activities including differentiated activities for different learners, including students with IEPs\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Plans have enough detail to guide teaching and learning\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"State planning forms are used\"\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Classroom Management\",\n" +
                "          \"criteria\": [\n" +
                "            {\n" +
                "              \"name\": \"Time is managed effectively to maximize students’ learning opportunities\",\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"Time is used constructively for teaching and learning of new materia\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Children are on-task and engaged in appropriate learning activities for most of the lesson time\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The lesson starts and finishes on time\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The pace of the lesson is neither too slow nor too rushed\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The teacher allocates their time fairly and equitably towards all children\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"Resources and learning materials are managed effectively\",\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"All resources and learning materials needed for the lesson are prepared in advance\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Children have access to adequate and appropriate resources and learning materials\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Resources and learning materials are stored neatly, safely and securely\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Children are partly responsible for the management of resources and learning materials\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Resources and learning materials are collected and put away at the end of the lesson\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"A positive, supportive climate for learning is created\",\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"The teacher expects, recognizes and rewards high standards of student behavior and achievement\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The classroom is orderly, safe and non-threatening\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Classroom rules and routines are clearly explained and children follow them\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Violence, threats or other forms of physical punishment are not used\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Children are encouraged to act responsibly and  learn cooperatively\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"A high quality physical learning environment is created\",\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"The environment is rich in print and visual displays that are related to the current learning objectives\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The classroom is clean, receives enough daylight light and is well ventilated\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The arrangement of furniture allows and supports a variety of teaching and learning activities\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The arrangement of furniture allows the teacher to move about the room to monitor and assist all children\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"There are designated areas of the classroom where topical displays or activity centers are located\"\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Teaching and learning\",\n" +
                "          \"criteria\": [\n" +
                "            {\n" +
                "              \"name\": \"The lesson is structured and objectives are clear\",\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"The lesson begins with a review of previous learning\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The learning objectives of the lesson are clearly stated\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The lesson contains a balance of teacher instruction and student activity\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The lesson is adapted to take account of student feedback\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The lesson ends with a review of what has been learned\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"New material is accurately and clearly presented, explained and placed within a meaningful context\",\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"New material or content is presented with authority in a logical and systematic way using clearly recognizable steps\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Explanations and demonstrations are clear, concise and easy to follow\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Concepts are explained in several different ways using relevant examples and meaningful contexts\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The language of instruction is appropriate to the needs of the children\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Verbal communication is clear, concise and understandable and is supported by print or other media\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"A variety of highly efficient interactive teaching and learning strategies are used\",\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"The teacher facilitates a wide variety of interactive teaching and cooperative learning strategies\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Children are actively and meaningfully engaged in appropriate, challenging activities and learning takes place through activity\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Learning activities include higher-order thinking, inquiry, observation, analysis and problem solving\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Students are given opportunities to apply their learning in meaningful contexts, including through the use of ICT\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Different learning activities are assigned according to individual children’s needs\"\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"Questioning and discussion are used effectively to support learning\",\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"The majority of teacher questions are open and enabling and encourage higher order thinking\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Questions are adapted to individual students’ needs and abilities, including those with IEPs\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The teacher responds positively to students’ responses and asks appropriate supplementary or follow-up questions\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Question and answer time is efficiently managed with appropriate routines such as wait time, hands up and other forms of signaling\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Discussions are managed so as to allow contributions from all students\"\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Student assessment\",\n" +
                "          \"criteria\": [\n" +
                "            {\n" +
                "              \"name\": \"Assessment is planned and integrated into the lesson and is based on observable and measurable criteria\",\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"Learning objectives and assessment criteria are explained to the children in language they can understand\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"A variety of assessment strategies are used in the lesson and feedback is given to students at strategic points\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The results of assessment are used to adapt current and future teaching and learning activities\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Individual assessment outcomes are recorded by the teacher or stored in a student portfolio or other record system\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"Children are involved in conducting self or peer assessments\"\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Evaluation and professional development\",\n" +
                "          \"criteria\": [\n" +
                "            {\n" +
                "              \"name\": \"The teacher reflects upon and evaluates their performance and participates in professional development\",\n" +
                "              \"subcriteria\": [\n" +
                "                {\n" +
                "                  \"name\": \"The teacher regularly makes thoughtful and accurate evaluations of lesson effectiveness based on children’s learning outcomes\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The teacher adjusts future lessons based on evaluation\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The teacher welcomes and acts upon feedback from observers, principal or other teachers\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The teacher participates in a school-based appraisal program\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"name\": \"The teacher actively seeks appropriate ways to enhance their professional knowledge and practice\"\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}");
    }
}
