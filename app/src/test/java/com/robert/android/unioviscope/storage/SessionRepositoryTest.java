package com.robert.android.unioviscope.storage;

import com.robert.android.unioviscope.domain.model.User;
import com.robert.android.unioviscope.domain.repository.SessionRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Clase test para la clase SessionRepositoryImpl.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.storage.SessionRepositoryImpl
 */
@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class SessionRepositoryTest {

    private SessionRepository mSessionRepository;
    private User mUser;
    private String mToken;

    @Before
    public void setUp() throws Exception {
        mSessionRepository = new SessionRepositoryImpl(RuntimeEnvironment.application);

        mUser = new User(null);
        mUser.setId(2L);
        mUser.setUserName("UOe1");

        mToken = "Bearer token";
    }

    @Test
    public void testGetUser() {
        assertNull(mSessionRepository.getUser());

        mSessionRepository.saveUser(mUser);
        assertEquals(mUser, mSessionRepository.getUser());
    }

    @Test
    public void testSaveUser() {
        mSessionRepository.deleteUser();
        assertNull(mSessionRepository.getUser());

        mSessionRepository.saveUser(mUser);
        assertEquals(mUser, mSessionRepository.getUser());
    }

    @Test
    public void testDeleteUser() {
        mSessionRepository.deleteUser();
        assertNull(mSessionRepository.getUser());

        mSessionRepository.saveUser(mUser);
        assertEquals(mUser, mSessionRepository.getUser());

        mSessionRepository.deleteUser();
        assertNull(mSessionRepository.getUser());
    }

    @Test
    public void testGetToken() {
        assertNull(mSessionRepository.getToken());

        mSessionRepository.saveToken(mToken);
        assertEquals(mToken, mSessionRepository.getToken());
    }

    @Test
    public void testSaveToken() {
        mSessionRepository.deleteToken();
        assertNull(mSessionRepository.getToken());

        mSessionRepository.saveToken(mToken);
        assertEquals(mToken, mSessionRepository.getToken());
    }

    @Test
    public void testDeleteUToken() {
        mSessionRepository.deleteToken();
        assertNull(mSessionRepository.getToken());

        mSessionRepository.saveToken(mToken);
        assertEquals(mToken, mSessionRepository.getToken());

        mSessionRepository.deleteToken();
        assertNull(mSessionRepository.getToken());
    }
}
