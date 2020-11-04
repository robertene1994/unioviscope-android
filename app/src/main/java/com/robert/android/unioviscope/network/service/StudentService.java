package com.robert.android.unioviscope.network.service;

import com.robert.android.unioviscope.domain.model.AccountCredentials;
import com.robert.android.unioviscope.domain.model.AttendanceCertificate;
import com.robert.android.unioviscope.domain.model.Session;
import com.robert.android.unioviscope.domain.model.Subject;
import com.robert.android.unioviscope.domain.model.User;
import com.robert.android.unioviscope.domain.model.types.GroupType;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Interfaz que contiene las operaciones mediante las cuales la aplicación se comunica con la API.
 *
 * @author Robert Ene
 */
public interface StudentService {
    /**
     * Método mediante el cual el estudiante inicia sesión en la aplicación.
     *
     * @param account las credenciales del estudiante.
     * @return la llamada asíncrona a la API.
     */
    @POST("common/logIn")
    Call<Void> logIn(@Body AccountCredentials account);

    /**
     * Método que recupera los detalles del estudiante que inicia sesión.
     *
     * @param userName el nombre de usuario para el que se recupera los detalles.
     * @return los detalles del estudiante.
     */
    @GET("common/findUserDetails")
    Call<User> getUserDetails(@Query("userName") String userName);

    /**
     * Método que valida la certificación de la asistencia.
     *
     * @param attendanceCertificate la certificación de la asistencia.
     * @return true si la certificación es válida, false de lo contrario.
     */
    @POST("student/verifyAttendanceCertificate")
    Call<Boolean> verifyAttendanceCertificate(@Body AttendanceCertificate attendanceCertificate);

    /**
     * Método que certifica la asistencia.
     *
     * @param certificate la certificación de la asistencia.
     * @param file        el fichero que contiene la foto capturada por el estudiante.
     * @return true si la asistencia es válida, false de lo contrario.
     */
    @Multipart
    @POST("student/certifyAttendance")
    Call<Boolean> certifyAttendance(@Part("certificate") AttendanceCertificate certificate,
                                    @Part MultipartBody.Part file);

    /**
     * Método que recupera las asignaturas asociadas a un determinado estudiante.
     *
     * @param studentId el id del estudiante.
     * @return las asignaturas asociadas al estudiante.
     */
    @GET("student/findLastYearSubjects")
    Call<List<Subject>> getSubjects(@Query("studentId") Long studentId);

    /**
     * Método que recupera las sesiones planificadas para una determinada asignatura y un determinado tipo de grupo
     * asociadas a un determinado estudiante.
     *
     * @param studentId el id del estudiante.
     * @param subjectId el id de la asignatura.
     * @param groupType el tipo de grupo de las sesiones.
     * @return las sesiones planificadas para la asignatura.
     */
    @GET("student/findLastYearSubjectSessions")
    Call<List<Session>> getSessions(@Query("studentId") Long studentId, @Query("subjectId") Long subjectId, @Query(
            "groupType") GroupType groupType);

    /**
     * Método que recupera una asistencia asociada a una determinada sesión y estudiante.
     *
     * @param studentId el id del estudiante.
     * @param sessionId el id de la sesión.
     * @return la asistencia asociada.
     */
    @GET("student/findStudentSessionAttendance")
    Call<ResponseBody> getAttendance(@Query("studentId") Long studentId, @Query("sessionId") Long sessionId);
}