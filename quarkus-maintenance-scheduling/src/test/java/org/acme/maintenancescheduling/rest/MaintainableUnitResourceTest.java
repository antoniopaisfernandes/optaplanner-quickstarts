/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.acme.maintenancescheduling.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.acme.maintenancescheduling.domain.MaintainableUnit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@QuarkusTest
public class MaintainableUnitResourceTest {

    @Test
    public void getAll() {
        List<MaintainableUnit> unitList = given()
                .when().get("/units")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList(".", MaintainableUnit.class);
        assertFalse(unitList.isEmpty());
        MaintainableUnit firstUnit = unitList.get(0);
        assertEquals("Switch 1", firstUnit.getUnitName());
    }

    @Test
    public void addAndRemove() {
        MaintainableUnit unit = given()
                .when()
                .contentType(ContentType.JSON)
                .body(new MaintainableUnit("Test unit"))
                .post("/units")
                .then()
                .statusCode(202)
                .extract().as(MaintainableUnit.class);

        given()
                .when()
                .delete("/units/{id}", unit.getId())
                .then()
                .statusCode(200);
    }
}
