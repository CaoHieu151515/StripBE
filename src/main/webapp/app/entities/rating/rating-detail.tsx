import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './rating.reducer';

export const RatingDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ratingEntity = useAppSelector(state => state.rating.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ratingDetailsHeading">
          <Translate contentKey="sTripBeApp.rating.detail.title">Rating</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ratingEntity.id}</dd>
          <dt>
            <span id="ratingID">
              <Translate contentKey="sTripBeApp.rating.ratingID">Rating ID</Translate>
            </span>
          </dt>
          <dd>{ratingEntity.ratingID}</dd>
          <dt>
            <span id="ratingTime">
              <Translate contentKey="sTripBeApp.rating.ratingTime">Rating Time</Translate>
            </span>
          </dt>
          <dd>{ratingEntity.ratingTime ? <TextFormat value={ratingEntity.ratingTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="ratingDriver">
              <Translate contentKey="sTripBeApp.rating.ratingDriver">Rating Driver</Translate>
            </span>
          </dt>
          <dd>{ratingEntity.ratingDriver}</dd>
          <dt>
            <span id="ratingType">
              <Translate contentKey="sTripBeApp.rating.ratingType">Rating Type</Translate>
            </span>
          </dt>
          <dd>{ratingEntity.ratingType}</dd>
          <dt>
            <Translate contentKey="sTripBeApp.rating.trip">Trip</Translate>
          </dt>
          <dd>{ratingEntity.trip ? ratingEntity.trip.id : ''}</dd>
          <dt>
            <Translate contentKey="sTripBeApp.rating.driver">Driver</Translate>
          </dt>
          <dd>{ratingEntity.driver ? ratingEntity.driver.id : ''}</dd>
          <dt>
            <Translate contentKey="sTripBeApp.rating.user">User</Translate>
          </dt>
          <dd>{ratingEntity.user ? ratingEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/rating" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/rating/${ratingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RatingDetail;
