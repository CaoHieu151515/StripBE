import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getTrips } from 'app/entities/trip/trip.reducer';
import { getEntities as getDrivers } from 'app/entities/driver/driver.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ReportType } from 'app/shared/model/enumerations/report-type.model';
import { ReportStatus } from 'app/shared/model/enumerations/report-status.model';
import { createEntity, getEntity, reset, updateEntity } from './report.reducer';

export const ReportUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const trips = useAppSelector(state => state.trip.entities);
  const drivers = useAppSelector(state => state.driver.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const reportEntity = useAppSelector(state => state.report.entity);
  const loading = useAppSelector(state => state.report.loading);
  const updating = useAppSelector(state => state.report.updating);
  const updateSuccess = useAppSelector(state => state.report.updateSuccess);
  const reportTypeValues = Object.keys(ReportType);
  const reportStatusValues = Object.keys(ReportStatus);

  const handleClose = () => {
    navigate(`/report${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTrips({}));
    dispatch(getDrivers({}));
    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.date = convertDateTimeToServer(values.date);

    const entity = {
      ...reportEntity,
      ...values,
      trip: trips.find(it => it.id.toString() === values.trip?.toString()),
      driver: drivers.find(it => it.id.toString() === values.driver?.toString()),
      user: users.find(it => it.id.toString() === values.user?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          date: displayDefaultDateTime(),
        }
      : {
          reportType: 'DRIVER_TO_USER',
          reportStatus: 'WAITING',
          ...reportEntity,
          date: convertDateTimeFromServer(reportEntity.date),
          trip: reportEntity?.trip?.id,
          driver: reportEntity?.driver?.id,
          user: reportEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sTripBeApp.report.home.createOrEditLabel" data-cy="ReportCreateUpdateHeading">
            <Translate contentKey="sTripBeApp.report.home.createOrEditLabel">Create or edit a Report</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="report-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sTripBeApp.report.reportID')}
                id="report-reportID"
                name="reportID"
                data-cy="reportID"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.report.reportType')}
                id="report-reportType"
                name="reportType"
                data-cy="reportType"
                type="select"
              >
                {reportTypeValues.map(reportType => (
                  <option value={reportType} key={reportType}>
                    {translate(`sTripBeApp.ReportType.${reportType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('sTripBeApp.report.date')}
                id="report-date"
                name="date"
                data-cy="date"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sTripBeApp.report.content')}
                id="report-content"
                name="content"
                data-cy="content"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.report.reportStatus')}
                id="report-reportStatus"
                name="reportStatus"
                data-cy="reportStatus"
                type="select"
              >
                {reportStatusValues.map(reportStatus => (
                  <option value={reportStatus} key={reportStatus}>
                    {translate(`sTripBeApp.ReportStatus.${reportStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="report-trip" name="trip" data-cy="trip" label={translate('sTripBeApp.report.trip')} type="select">
                <option value="" key="0" />
                {trips
                  ? trips.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="report-driver" name="driver" data-cy="driver" label={translate('sTripBeApp.report.driver')} type="select">
                <option value="" key="0" />
                {drivers
                  ? drivers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="report-user" name="user" data-cy="user" label={translate('sTripBeApp.report.user')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/report" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ReportUpdate;
