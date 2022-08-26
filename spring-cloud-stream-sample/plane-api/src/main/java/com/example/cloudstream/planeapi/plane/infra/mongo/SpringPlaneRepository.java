package com.example.cloudstream.planeapi.plane.infra.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

interface SpringPlaneRepository extends MongoRepository<PlaneDocument, String> {
}
