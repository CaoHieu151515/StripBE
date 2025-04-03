import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getDrivers } from 'app/entities/driver/driver.reducer';
import { getEntities as getUserDetails } from 'app/entities/user-detail/user-detail.reducer';
import { DriverPointHistoryStatus } from 'app/shared/model/enumerations/driver-point-history-status.model';
import { createEntity, getEntity, reset, updateEntity } from './driver-point-history.reducer';

export const DriverPointHistoryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const drivers = useAppSelector(state => state.driver.entities);
  const userDetails = useAppSelector(state => state.userDetail.entities);
  const driverPointHistoryEntity = useAppSelector(state => state.driverPointHistory.entity);
  const loading = useAppSelector(state => state.driverPointHistory.loading);
  const updating = useAppSelector(state => state.driverPointHistory.updating);
  const updateSuccess = useAppSelector(state => state.driverPointHistory.updateSuccess);
  const driverPointHistoryStatusValues = Object.keys(DriverPointHistoryStatus);

  const handleClose = () => {
    navigate(`/driver-point-history${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getDrivers({}));
    dispatch(getUserDetails({}));
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
    if (values.point !== undefined && typeof values.point !== 'number') {
      values.point = Number(values.point);
    }
    values.date = convertDateTimeToServer(values.date);

    const entity = {
      ...driverPointHistoryEntity,
      ...values,
      driver: drivers.find(it => it.id.toString() === values.driver?.toString()),
      userDetail: userDetails.find(it => it.id.toString() === values.userDetail?.toString()),
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
          status: 'DONE',
          ...driverPointHistoryEntity,
          date: convertDateTimeFromServer(driverPointHistoryEntity.date),
          driver: driverPointHistoryEntity?.driver?.id,
          userDetail: driverPointHistoryEntity?.userDetail?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sTripBeApp.driverPointHistory.home.createOrEditLabel" data-cy="DriverPointHistoryCreateUpdateHeading">
            <Translate contentKey="sTripBeApp.driverPointHistory.home.createOrEditLabel">Create or edit a DriverPointHistory</Translate>
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
                  id="driver-point-history-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sTripBeApp.driverPointHistory.pointId')}
                id="driver-point-history-pointId"
                name="pointId"
                data-cy="pointId"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.driverPointHistory.point')}
                id="driver-point-history-point"
                name="point"
                data-cy="point"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.driverPointHistory.reason')}
                id="driver-point-history-reason"
                name="reason"
                data-cy="reason"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.driverPointHistory.date')}
                id="driver-point-history-date"
                name="date"
                data-cy="date"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sTripBeApp.driverPointHistory.status')}
                id="driver-point-history-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {driverPointHistoryStatusValues.map(driverPointHistoryStatus => (
                  <option value={driverPointHistoryStatus} key={driverPointHistoryStatus}>
                    {translate(`sTripBeApp.DriverPointHistoryStatus.${driverPointHistoryStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="driver-point-history-driver"
                name="driver"
                data-cy="driver"
                label={translate('sTripBeApp.driverPointHistory.driver')}
                type="select"
              >
                <option value="" key="0" />
                {drivers
                  ? drivers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="driver-point-history-userDetail"
                name="userDetail"
                data-cy="userDetail"
                label={translate('sTripBeApp.driverPointHistory.userDetail')}
                type="select"
              >
                <option value="" key="0" />
                {userDetails
                  ? userDetails.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/driver-point-history" replace color="info">
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

export default DriverPointHistoryUpdate;
