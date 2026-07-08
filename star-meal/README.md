# StarMeal

StarMeal is the first real app built with Forge.

The name is temporary and can change later. The product goal is stable: help a user capture meals, estimate nutrition, and flag possible profile-based concerns from ingredients and portion context.

## Safety Boundary

StarMeal should never claim to diagnose, guarantee food safety, or replace medical advice.

The app should:

- estimate nutrition with visible confidence
- ask the user to confirm ingredients and portions
- flag possible concerns from the user's profile
- explain uncertainty clearly
- keep photo/provider implementation behind an analyzer interface

The first version uses a local rule-based analyzer so the product flow can be built without uploading photos or requiring an API key. Camera capture and provider-backed image analysis should plug into the same analyzer contract later.
