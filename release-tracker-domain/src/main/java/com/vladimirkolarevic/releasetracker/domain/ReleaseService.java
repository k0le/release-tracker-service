package com.vladimirkolarevic.releasetracker.domain;

import com.vladimirkolarevic.releasetracker.domain.exception.ReleaseTrackerException;

import java.util.List;
import java.util.UUID;

public interface ReleaseService {

     Release save(Release release) throws ReleaseTrackerException;

     Release get(UUID id) throws ReleaseTrackerException;

     List<Release> list() throws ReleaseTrackerException;

     void delete(UUID uuid) throws ReleaseTrackerException;

     Long count();

}
