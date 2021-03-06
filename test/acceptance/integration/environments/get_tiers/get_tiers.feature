# -*- coding: utf-8 -*-
Feature: Get the list of tiers of an environment in a tenant

    As a fi-ware user
    I want to be able to get the list of tiers of an environment in a tenant
    so that I know the tiers my environment will have when I create new instances

    @happy_path
    Scenario: Get a list of just one tier
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | nameqa | descqa      |
        And a tier has already been added to the environment "nameqa" with data:
            | name       |
            | tiernameqa |
        When I request the list of tiers of the environment "nameqa"
        Then I receive an "OK" response with "1" item in the list
        And there is a tier in the list with data:
            | name       |
            | tiernameqa |
                
    Scenario: Get an empty list of tiers
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | nameqa | descqa      |
        When I request the list of tiers of the environment "nameqa"
        Then I receive an "OK" response with no content
                
    Scenario Outline: Get a list of just one tier with different valid data
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | <name> | descqa      |
        And a tier has already been added to the environment "<name>" with data:
            | name       |
            | <tiername> |
        When I request the list of tiers of the environment "<name>"
        Then I receive an "OK" response with "1" item in the list
        And there is a tier in the list with data:
            | name       |
            | <tiername> |
        
        Examples:
            | name    | tiername                |
            | nameqa1 | a                       |
            | nameqa2 | nameqa-1                |
            | nameqa3 | [STRING_WITH_LENGTH_30] |
                
    Scenario: Get a list with several tiers
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | nameqa | descqa      |
        And a tier has already been added to the environment "nameqa" with data:
            | name        |
            | tiernameqa1 |
        And a tier has already been added to the environment "nameqa" with data:
            | name        |
            | tiernameqa2 |
        And a tier has already been added to the environment "nameqa" with data:
            | name        |
            | tiernameqa3 |
        When I request the list of tiers of the environment "nameqa"
        Then I receive an "OK" response with "3" items in the list
        And there is a tier in the list with data:
            | name        |
            | tiernameqa1 |
        And there is a tier in the list with data:
            | name        |
            | tiernameqa2 |
        And there is a tier in the list with data:
            | name        |
            | tiernameqa3 |
                
    Scenario: Get a list with several tiers with products
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | nameqa | descqa      |
        And a tier has already been added to the environment "nameqa" with data:
            | name        | products |
            | tiernameqa1 | git=1.7  |
        And a tier has already been added to the environment "nameqa" with data:
            | name        | products                 |
            | tiernameqa2 | git=1.7,mediawiki=1.17.0 |
        When I request the list of tiers of the environment "nameqa"
        Then I receive an "OK" response with "2" items in the list
        And there is a tier in the list with data:
            | name        | products |
            | tiernameqa1 | git=1.7  |
        And there is a tier in the list with data:
            | name        | products                 |
            | tiernameqa2 | git=1.7,mediawiki=1.17.0 |

    Scenario: Get a list with several tiers with networks
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | nameqa | descqa      |
        And a tier has already been added to the environment "nameqa" with data:
            | name        | networks |
            | tiernameqa1 | netqa1   |
        And a tier has already been added to the environment "nameqa" with data:
            | name        | networks      |
            | tiernameqa2 | netqa1,netqa2 |
        When I request the list of tiers of the environment "nameqa"
        Then I receive an "OK" response with "2" items in the list
        And there is a tier in the list with data:
            | name        | networks |
            | tiernameqa1 | netqa1   |
        And there is a tier in the list with data:
            | name        | networks      |
            | tiernameqa2 | netqa1,netqa2 |

    Scenario: Get a list with many tiers with products and networks
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | nameqa | descqa      |
        And a tier has already been added to the environment "nameqa" with data:
            | name        |
            | tiernameqa1 |
        And a tier has already been added to the environment "nameqa" with data:
            | name        | products |
            | tiernameqa2 | git=1.7  |
        And a tier has already been added to the environment "nameqa" with data:
            | name        | products                 |
            | tiernameqa3 | git=1.7,mediawiki=1.17.0 |
        And a tier has already been added to the environment "nameqa" with data:
            | name        | networks |
            | tiernameqa4 | netqa1   |
        And a tier has already been added to the environment "nameqa" with data:
            | name        | networks      |
            | tiernameqa5 | netqa1,netqa2 |
        And a tier has already been added to the environment "nameqa" with data:
            | name        | products | networks |
            | tiernameqa6 | git=1.7  | netqa1   |
        And a tier has already been added to the environment "nameqa" with data:
            | name        | products                 | networks      |
            | tiernameqa7 | git=1.7,mediawiki=1.17.0 | netqa1,netqa2 |
        When I request the list of tiers of the environment "nameqa"
        Then I receive an "OK" response with "7" items in the list
        And there is a tier in the list with data:
            | name        |
            | tiernameqa1 |
        And there is a tier in the list with data:
            | name        | products |
            | tiernameqa2 | git=1.7  |
        And there is a tier in the list with data:
            | name        | products                 |
            | tiernameqa3 | git=1.7,mediawiki=1.17.0 |
        And there is a tier in the list with data:
            | name        | networks |
            | tiernameqa4 | netqa1   |
        And there is a tier in the list with data:
            | name        | networks      |
            | tiernameqa5 | netqa1,netqa2 |
        And there is a tier in the list with data:
            | name        | products | networks |
            | tiernameqa6 | git=1.7  | netqa1   |
        And there is a tier in the list with data:
            | name        | products                 | networks      |
            | tiernameqa7 | git=1.7,mediawiki=1.17.0 | netqa1,netqa2 |
